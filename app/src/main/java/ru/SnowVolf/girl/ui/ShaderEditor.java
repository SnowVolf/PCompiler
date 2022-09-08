package ru.SnowVolf.girl.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.RegexPattern;
import ru.SnowVolf.pcompiler.settings.Preferences;

/**
 * Created by Snow Volf on 03.11.2017, 15:45
 */

public class ShaderEditor extends TextInputEditText {
    private static final Pattern PATTERN_NUMBERS = RegexPattern.INSTANCE.getNUMBERS();
    private static final Pattern PATTERN_NUMBER_ATTRIBUTE = RegexPattern.INSTANCE.getOPERATOR();
    private static final Pattern PATTERN_ATTRIBUTE = RegexPattern.INSTANCE.getATTRIBUTE();
    private static final Pattern PATTERN_KEYWORDS = RegexPattern.INSTANCE.getCOMMON_SYMBOLS();
    private static final Pattern PATTERN_SUB_ATTRIBUTE = RegexPattern.INSTANCE.getSUB_ATTRIBUTE();
    private static final Pattern PATTERN_COMMENTS_STRING = RegexPattern.INSTANCE.getSTRING();
    private static final Pattern PATTERN_TRAILING_WHITE_SPACE = Pattern.compile(
            "[\\t ]+$",
            Pattern.MULTILINE);
    private final Handler updateHandler = new Handler();
    private ShaderText.OnTextChangedListener onTextChangedListener;
    private int updateDelay = 2000;
    private int errorLine = 0;
    private boolean dirty = false;
    private boolean modified = true;
    private int colorNumber;
    private int colorKeyword;
    private int colorBuiltin;
    private int colorComment;
    private int colorAttr;
    private int colorOperator;
    private final Runnable updateRunnable =
            new Runnable() {
                @Override
                public void run() {
                    Editable e = getText();

                    if (onTextChangedListener != null)
                        onTextChangedListener.onTextChanged(
                                e.toString());

                    highlightWithoutChange(e);
                }
            };
    private int tabWidthInCharacters = 0;
    private int tabWidth = 0;

    public ShaderEditor(Context context) {
        super(context);

        init();
    }

    public ShaderEditor(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private static void clearSpans(Editable e) {
        // remove foreground color spans
        {
            ForegroundColorSpan spans[] = e.getSpans(
                    0,
                    e.length(),
                    ForegroundColorSpan.class);

            for (int n = spans.length; n-- > 0; )
                e.removeSpan(spans[n]);
        }

        // remove background color spans
        {
            BackgroundColorSpan spans[] = e.getSpans(
                    0,
                    e.length(),
                    BackgroundColorSpan.class);

            for (int n = spans.length; n-- > 0; )
                e.removeSpan(spans[n]);
        }
    }

    public void setOnTextChangedListener(ShaderText.OnTextChangedListener listener) {
        onTextChangedListener = listener;
    }

    public void setUpdateDelay(int ms) {
        updateDelay = ms;
    }

    public void setTabWidth(int characters) {
        if (tabWidthInCharacters == characters)
            return;

        tabWidthInCharacters = characters;
        tabWidth = Math.round(
                getPaint().measureText("m") *
                        characters);
    }

    public boolean hasErrorLine() {
        return errorLine > 0;
    }

    public void setErrorLine(int line) {
        errorLine = line;
    }

    public void updateHighlighting() {
        highlightWithoutChange(getEditableText());
    }

    public boolean isModified() {
        return dirty;
    }

    public void setTextHighlighted(CharSequence text) {
        if (text == null)
            text = "";

        cancelUpdate();

        errorLine = 0;
        dirty = false;

        modified = false;
        setText(highlight(new SpannableStringBuilder(text)));
        modified = true;

        if (onTextChangedListener != null)
            onTextChangedListener.onTextChanged(text.toString());
    }

    public String getCleanText() {
        return PATTERN_TRAILING_WHITE_SPACE
                .matcher(getText())
                .replaceAll("");
    }

    public void insertTab() {
        int start = getSelectionStart();
        int end = getSelectionEnd();

        getText().replace(
                Math.min(start, end),
                Math.max(start, end),
                "\t",
                0,
                1);
    }

    private void init() {
        //setHorizontallyScrolling(true);

        setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    if (modified &&
                            end - start == 1 &&
                            start < source.length() &&
                            dstart < dest.length()) {
                        char c = source.charAt(start);

                        if (c == '\n')
                            return autoIndent(
                                    source,
                                    dest,
                                    dstart,
                                    dend);
                    }

                    return source;
                }});

        addTextChangedListener(
                new TextWatcher() {
                    private int start = 0;
                    private int count = 0;

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {
                        this.start = start;
                        this.count = count;
                    }

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable e) {
                        cancelUpdate();
                        convertTabs(e, start, count);

                        if (!modified)
                            return;

                        dirty = true;
                        updateHandler.postDelayed(
                                updateRunnable,
                                updateDelay);
                    }
                });

        setSyntaxColors();
    }

    private void setSyntaxColors() {
        colorNumber = Preferences.INSTANCE.isArtaSyntaxAllowed() ? ContextCompat.getColor(getContext(),
                R.color.syntax_arta_num) : ContextCompat.getColor(getContext(), R.color.syntax_num);
        colorKeyword = Preferences.INSTANCE.isArtaSyntaxAllowed() ? ContextCompat.getColor(getContext(),
                R.color.syntax_arta_keyword) : ContextCompat.getColor(getContext(), R.color.syntax_keyword);
        colorBuiltin = ContextCompat.getColor(getContext(), R.color.syntax_sub_element);
        colorComment = Preferences.INSTANCE.isArtaSyntaxAllowed() ? ContextCompat.getColor(getContext(),
                R.color.syntax_arta_string) : ContextCompat.getColor(getContext(), R.color.syntax_string);
        colorAttr = Preferences.INSTANCE.isArtaSyntaxAllowed() ? ContextCompat.getColor(getContext(),
                R.color.syntax_arta_element) : ContextCompat.getColor(getContext(), R.color.syntax_element);
        colorOperator = Preferences.INSTANCE.isArtaSyntaxAllowed() ? ContextCompat.getColor(getContext(),
                R.color.syntax_arta_num_attribute) : ContextCompat.getColor(getContext(), R.color.syntax_num_attribute);

    }

    private void cancelUpdate() {
        updateHandler.removeCallbacks(updateRunnable);
    }

    private void highlightWithoutChange(Editable e) {
        modified = false;
        highlight(e);
        modified = true;
    }

    private Editable highlight(Editable e) {
        try {
            // don't use e.clearSpans() because it will
            // remove too much
            clearSpans(e);

            if (e.length() == 0)
                return e;

            for (Matcher m = PATTERN_NUMBERS.matcher(e); m.find(); )
                e.setSpan(new ForegroundColorSpan(colorNumber), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            for (Matcher m = PATTERN_ATTRIBUTE.matcher(e); m.find(); )
                e.setSpan(new ForegroundColorSpan(colorAttr), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            for (Matcher m = PATTERN_KEYWORDS.matcher(e); m.find(); )
                e.setSpan(new ForegroundColorSpan(colorKeyword), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            for (Matcher m = PATTERN_SUB_ATTRIBUTE.matcher(e); m.find(); )
                e.setSpan(new ForegroundColorSpan(colorBuiltin), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            for(Matcher m = PATTERN_NUMBER_ATTRIBUTE.matcher(e); m.find(); ) {
                e.setSpan(new ForegroundColorSpan(colorOperator), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for(Matcher m = Pattern.compile("\\\"(.*?)\\\"|\\\'(.*?)\\\'").matcher(e); m.find(); ) {
                ForegroundColorSpan spans[] = e.getSpans(m.start(), m.end(), ForegroundColorSpan.class);
                for(ForegroundColorSpan span : spans)
                    e.removeSpan(span);
                e.setSpan(new ForegroundColorSpan(Color.parseColor("#81C784")), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (Matcher m = PATTERN_COMMENTS_STRING.matcher(e); m.find(); ) {
                ForegroundColorSpan spans[] = e.getSpans(m.start(), m.end(), ForegroundColorSpan.class);
                for(ForegroundColorSpan span : spans)
                    e.removeSpan(span);
                e.setSpan(new ForegroundColorSpan(colorComment), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (IllegalStateException ex) {
            // raised by Matcher.start()/.end() when
            // no successful match has been made what
            // shouldn't ever happen because of find()
        }

        return e;
    }

    private CharSequence autoIndent(
            CharSequence source,
            Spanned dest,
            int dstart,
            int dend) {
        String indent = "";
        int istart = dstart - 1;

        // find start of this line
        boolean dataBefore = false;
        int pt = 0;

        for (; istart > -1; --istart) {
            char c = dest.charAt(istart);

            if (c == '\n')
                break;

            if (c != ' ' &&
                    c != '\t') {
                if (!dataBefore) {
                    // indent always after those characters
                    if (c == '{' ||
                            c == '+' ||
                            c == '-' ||
                            c == '*' ||
                            c == '/' ||
                            c == '%' ||
                            c == '^' ||
                            c == '=')
                        pt--;

                    dataBefore = true;
                }

                // parenthesis counter
                if (c == '(')
                    --pt;
                else if (c == ')')
                    ++pt;
            }
        }

        // copy indent of this line into the next
        if (istart > -1) {
            char charAtCursor = dest.charAt(dstart);
            int iend;

            for (iend = ++istart;
                 iend < dend;
                 ++iend) {
                char c = dest.charAt(iend);

                // auto expand comments
                if (charAtCursor != '\n' &&
                        c == '/' &&
                        iend + 1 < dend &&
                        dest.charAt(iend) == c) {
                    iend += 2;
                    break;
                }

                if (c != ' ' &&
                        c != '\t')
                    break;
            }

            indent += dest.subSequence(istart, iend);
        }

        // add new indent
        if (pt < 0)
            indent += "\t";

        // append white space of previous line and new indent
        return source + indent;
    }

    private void convertTabs(Editable e, int start, int count) {
        if (tabWidth < 1)
            return;

        String s = e.toString();

        for (int stop = start + count;
             (start = s.indexOf("\t", start)) > -1 && start < stop;
             ++start)
            e.setSpan(
                    new TabWidthSpan(),
                    start,
                    start + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public interface OnTextChangedListener {
        void onTextChanged(String text);
    }

    private class TabWidthSpan extends ReplacementSpan {
        @Override
        public int getSize(
                Paint paint,
                CharSequence text,
                int start,
                int end,
                Paint.FontMetricsInt fm) {
            return tabWidth;
        }

        @Override
        public void draw(
                Canvas canvas,
                CharSequence text,
                int start,
                int end,
                float x,
                int top,
                int y,
                int bottom,
                Paint paint) {
        }
    }
}

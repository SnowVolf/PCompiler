package ru.SnowVolf.girl.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import java.util.regex.Matcher;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.ReactiveBuilder;
import ru.SnowVolf.pcompiler.patch.RegexPattern;
import ru.SnowVolf.pcompiler.settings.Preferences;

/**
 * Created by Snow Volf on 01.11.2017, 16:52
 */

public class GirlHighlightTextView extends AppCompatTextView {
    public GirlHighlightTextView(Context context) {
        super(context);
        init();
    }

    public GirlHighlightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GirlHighlightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init(){
        Spannable spannable;
        if (Preferences.isMonospaceFontAllowed()) {
            setTypeface(ResourcesCompat.getFont(getContext(), R.font.RobotoMono_Regular));
        }
        setTextSize(Preferences.getFontSize());
        setText(ReactiveBuilder.build());
        spannable = new SpannableString(getText());
        Matcher matcherAttr = RegexPattern.ATTRIBUTE.matcher(getText());
        Matcher matcherSubAttr = RegexPattern.SUB_ATTRIBUTE.matcher(getText());
        Matcher matcherBraces = RegexPattern.COMMON_SYMBOLS.matcher(getText());
        Matcher matcherNumAttr = RegexPattern.OPERATOR.matcher(getText());
        Matcher matcherNum = RegexPattern.NUMBERS.matcher(getText());
        Matcher matcherString = RegexPattern.STRING.matcher(getText());

        while (matcherAttr.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_element) : ContextCompat.getColor(App.getContext(), R.color.syntax_element)), matcherAttr.start(), matcherAttr.end(), 33);
        }

        while (matcherSubAttr.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_sub_element) : ContextCompat.getColor(App.getContext(), R.color.syntax_sub_element)), matcherSubAttr.start(), matcherSubAttr.end(), 33);
        }

        while (matcherBraces.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_keyword) : ContextCompat.getColor(App.getContext(), R.color.syntax_keyword)), matcherBraces.start(), matcherBraces.end(), 33);
        }

        while (matcherNumAttr.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_num_attribute) : ContextCompat.getColor(App.getContext(), R.color.syntax_num_attribute)), matcherNumAttr.start(), matcherNumAttr.end(), 33);
        }

        while (matcherNum.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_num) : ContextCompat.getColor(App.getContext(), R.color.syntax_num)), matcherNum.start(), matcherNum.end(), 33);
        }

        while (matcherString.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_string) : ContextCompat.getColor(App.getContext(), R.color.syntax_string)), matcherString.start(), matcherString.end(), 33);
        }
        setText(spannable);
    }
}

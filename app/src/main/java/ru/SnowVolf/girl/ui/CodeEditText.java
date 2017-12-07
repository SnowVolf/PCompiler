package ru.SnowVolf.girl.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.util.ThemeWrapper;

/**
 * Created by Snow Volf on 03.11.2017, 15:57
 */

public class CodeEditText extends ShaderEditor {
    /** The line numbers paint */
    protected Paint mPaintNumbers;
    /** The line numbers paint */
    protected Paint mPaintHighlight;
    /** the offset value in dp */
    protected int mPaddingDP = 6;
    /** the padding scaled */
    protected int mPadding, mLinePadding;
    /** the scale for desnity pixels */
    protected float mScale;
    /** the Max size of the view */
    protected Point mMaxSize;

    /** the highlighted line index */
    protected int mHighlightedLine;
    protected int mHighlightStart;

    protected Rect mDrawingRect, mLineBounds;

    public CodeEditText(Context context) {
        this(context, null);
    }

    public CodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(Preferences.INSTANCE.getFontSize());
        mPaintNumbers = new Paint();
        setTypeface(Preferences.INSTANCE.isMonospaceFontAllowed() ?
                ResourcesCompat.getFont(getContext(), R.font.mono) :
        Typeface.DEFAULT);
        mPaintNumbers.setAntiAlias(true);
        mPaintNumbers.setColor(ThemeWrapper.isLightTheme() ? Color.BLACK : Color.WHITE);

        mPaintHighlight = new Paint();

        mScale = context.getResources().getDisplayMetrics().density;
        mPadding = (int) (mPaddingDP * mScale);

        mHighlightedLine = mHighlightStart = -1;

        mDrawingRect = new Rect();
        mLineBounds = new Rect();

    }

    @Override
    public void onDraw(Canvas canvas) {
        int count, lineX, baseline;

        count = getLineCount();

//        if (Settings.SHOW_LINE_NUMBERS) {
            int padding = (int) (Math.floor(Math.log10(count)) + 1);
            padding = (int) ((padding * mPaintNumbers.getTextSize()) + mPadding + (Preferences.INSTANCE.getFontSize()
                    * mScale * 0.5));
            if (mLinePadding != padding) {
                mLinePadding = padding;
                setPadding(mLinePadding, mPadding, mPadding, mPadding);
            }
//        }

        // get the drawing boundaries
        getDrawingRect(mDrawingRect);

        // display current line
        computeLineHighlight();

        // draw line numbers
        lineX = (int) (mDrawingRect.left + mLinePadding - (Preferences.INSTANCE.getFontSize()
                * mScale * 0.5));
        int min = 0;
        int max = count;
        getLineBounds(0, mLineBounds);
        int startBottom = mLineBounds.bottom;
        int startTop = mLineBounds.top;
        getLineBounds(count - 1, mLineBounds);
        int endBottom = mLineBounds.bottom;
        int endTop = mLineBounds.top;
        if (count > 1 && endBottom > startBottom && endTop > startTop) {
            min = Math.max(min, ((mDrawingRect.top - startBottom) * (count - 1)) / (endBottom - startBottom));
            max = Math.min(max, ((mDrawingRect.bottom - startTop) * (count - 1)) / (endTop - startTop) + 1);
        }
        for (int i = min; i < max; i++) {
            baseline = getLineBounds(i, mLineBounds);
            if ((mMaxSize != null) && (mMaxSize.x < mLineBounds.right)) {
                mMaxSize.x = mLineBounds.right;
            }

//            if (i == mHighlightedLine) {
//                canvas.drawRect(mLineBounds, mPaintHighlight);
//            }
            canvas.drawText("" + (i + 1), mDrawingRect.left + mPadding,
                        baseline, mPaintNumbers);
            canvas.drawLine(lineX, mDrawingRect.top, lineX,
                        mDrawingRect.bottom, mPaintNumbers);

        }
        getLineBounds(count - 1, mLineBounds);
        if (mMaxSize != null) {
            mMaxSize.y = mLineBounds.bottom;
            mMaxSize.x = Math.max(mMaxSize.x + mPadding - mDrawingRect.width(),
                    0);
            mMaxSize.y = Math.max(
                    mMaxSize.y + mPadding - mDrawingRect.height(), 0);
        }

        super.onDraw(canvas);
    }

    protected void computeLineHighlight() {
        int i, line, selStart;
        String text;

        if (!isEnabled()) {
            mHighlightedLine = -1;
            return;
        }

        selStart = getSelectionStart();
        if (mHighlightStart != selStart) {
            text = getText().toString();

            line = i = 0;
            while (i < selStart) {
                i = text.indexOf("\n", i);
                if (i < 0) {
                    break;
                }
                if (i < selStart) {
                    ++line;
                }
                ++i;
            }
            mHighlightedLine = line;
        }
    }

    private static int countPatterns(String pattern, CharSequence text){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()){
            count++;
        }
        return count;
    }
}


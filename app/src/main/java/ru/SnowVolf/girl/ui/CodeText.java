package ru.SnowVolf.girl.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.res.ResourcesCompat;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.util.ThemeWrapper;

/**
 * Created by Snow Volf on 03.11.2017, 16:23
 */

public class CodeText extends ShaderText {
    private Context context;
    private transient Paint paint = new Paint();
    private transient Paint bgPaint = new Paint();
    private Layout layout;

    public CodeText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(App.getColorFromAttr(getContext(), android.R.attr.windowBackground));

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        if (Preferences.INSTANCE.isMonospaceFontAllowed()){
            setTypeface(ResourcesCompat.getFont(getContext(), R.font.mono));
        }
        setTextSize(Preferences.INSTANCE.getFontSize());
        paint.setColor(Color.parseColor(ThemeWrapper.INSTANCE.isLightTheme() ? "#000000" : "#fafafa"));
        paint.setTextSize(getPixels(14));
        getViewTreeObserver().addOnGlobalLayoutListener(() -> layout = getLayout());
    }

    private int getDigitCount() {
        int count = 0;
        int len = getLineCount();
        while (len > 0) {
            count++;
            len /= 10;
        }
        return count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int padding = (int) getPixels(getDigitCount() * 10 + 10);
        setPadding(padding, 0, 0, 0);

        int scrollY = getScrollY();
        int firstLine = layout.getLineForVertical(scrollY), lastLine;

        try {
            lastLine = layout.getLineForVertical(scrollY + (getHeight() - getExtendedPaddingTop() - getExtendedPaddingBottom()));
        } catch (NullPointerException npe) {
            lastLine = layout.getLineForVertical(scrollY + (getHeight() - getPaddingTop() - getPaddingBottom()));
        }

        //the y position starts at the baseline of the first line
        int positionY = getBaseline() + (layout.getLineBaseline(firstLine) - layout.getLineBaseline(0));
        drawLineNumber(canvas, layout, positionY, firstLine);
        for (int i = firstLine + 1; i <= lastLine; i++) {
            //get the next y position using the difference between the current and last baseline
            positionY += layout.getLineBaseline(i) - layout.getLineBaseline(i - 1);
            drawLineNumber(canvas, layout, positionY, i);
        }
        super.onDraw(canvas);
    }

    private void drawLineNumber(Canvas canvas, Layout layout, int positionY, int line) {
        int positionX = (int) layout.getLineLeft(line);
        canvas.drawText(String.valueOf(line + 1), positionX + getPixels(2), positionY, paint);

    }

    private float getPixels(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}

package ru.SnowVolf.girl.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import java.io.IOException;

import ru.SnowVolf.pcompiler.settings.Preferences;

/**
 * Created by Snow Volf on 25.09.2017, 21:02
 */

public class GirlEditText extends TextInputEditText {

    public GirlEditText(Context context) {
        super(context);
        init();
    }

    public GirlEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GirlEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if (Preferences.isMonospaceFontAllowed()){
            final Typeface mono = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoMono-Regular.ttf");
            setTypeface(mono);
        }
        setTextSize(Preferences.getFontSize());
    }
}

package ru.SnowVolf.girl.ui;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;

import ru.SnowVolf.pcompiler.R;
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
        if (Preferences.INSTANCE.isMonospaceFontAllowed()){
            setTypeface(ResourcesCompat.getFont(getContext(), R.font.mono));
        }
        setTextSize(Preferences.INSTANCE.getFontSize());
    }
}

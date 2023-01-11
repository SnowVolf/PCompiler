package ru.svolf.girl.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.settings.Preferences;

/**
 * Created by Snow Volf on 25.09.2017, 21:02
 */

public class GirlEditText extends AppCompatEditText {

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
        if (!isInEditMode()) {
            if (Preferences.INSTANCE.isMonospaceFontAllowed()) {
                setTypeface(ResourcesCompat.getFont(getContext(), R.font.mono));
            }
            setTextSize(Preferences.INSTANCE.getFontSize());
        }
    }

    public void clear(){
        setText("");
    }
}

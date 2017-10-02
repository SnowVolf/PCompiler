package ru.SnowVolf.girl.preference;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.AttributeSet;

/*
* Исправляет самопроизвольные переключения настроек в киткате.
* Пи*дец, да.
* */
public class SwitchGirl extends SwitchPreferenceCompat {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchGirl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SwitchGirl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwitchGirl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchGirl(Context context) {
        super(context);
    }
}

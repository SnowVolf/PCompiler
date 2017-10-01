package ru.SnowVolf.girl.preference;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

/**
 * Created by Snow Volf on 25.09.2017, 20:35
 */

public class GirlPreference extends Preference {
    public GirlPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public GirlPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GirlPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GirlPreference(Context context) {
        super(context);
        init();
    }

    private void init(){
        setSummary(get());
    }

    private String get(){
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getKey(), "");
    }
}

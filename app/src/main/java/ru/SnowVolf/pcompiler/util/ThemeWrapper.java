package ru.SnowVolf.pcompiler.util;

import android.app.Activity;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;

/**
 * Created by Snow Volf on 19.08.2017, 12:14
 */

public abstract class ThemeWrapper {
    public enum Theme{
        LIGHT,
        DARK
    }

    public static void applyTheme(Activity ctx){
        int theme;
        switch (Theme.values()[getThemeIndex()]){
            case LIGHT:
                theme = R.style.AppTheme;
                break;
            case DARK:
                theme = R.style.AppTheme_Dark;
                break;
            default:
                theme = R.style.AppTheme;
                break;
        }
        ctx.setTheme(theme);
    }

    private static int getThemeIndex() {
        return Integer.parseInt(App.ctx().getPreferences().getString("ui.theme", String.valueOf(ThemeWrapper.Theme.LIGHT.ordinal())));
    }

    public static int getTheme(){
        int theme;
        switch (Theme.values()[getThemeIndex()]){
            case LIGHT:
                theme = R.style.DialogLight;
                break;
            case DARK:
                theme = R.style.DialogDark;
                break;
            default:
                theme = R.style.DialogLight;
                break;
        }
        return theme;
    }

    public static boolean isLightTheme(){
        return getThemeIndex() == Theme.LIGHT.ordinal();
    }
}

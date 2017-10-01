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
        DARK,
        DARK_GLASS
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
            case DARK_GLASS:
                theme = R.style.AppTheme_Dark_Glass;
                break;
            default:
                theme = R.style.AppTheme;
                break;
        }
        ctx.setTheme(theme);
    }

    public static int getThemeIndex() {
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
            case DARK_GLASS:
                theme = R.style.DialogDark_Glass;
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

package ru.SnowVolf.pcompiler;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.util.Constants;
import ru.SnowVolf.pcompiler.util.StringWrapper;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.STACK_TRACE;

/**
 * Created by Snow Volf on 17.08.2017, 15:24
 */
@ReportsCrashes(
        mailTo = "svolf15@yandex.ru",
        mode = ReportingInteractionMode.NOTIFICATION,
        resDialogTheme = R.style.DialogDark,
        resDialogTitle = R.string.error_crashed,
        resDialogText = R.string.error_occurred,
        resNotifTitle = R.string.app_name,
        resDialogIcon = R.mipmap.ic_launcher,
        resNotifText = R.string.error_occurred,
        resNotifTickerText = R.string.error_crashed,
        customReportContent = {APP_VERSION_CODE, APP_VERSION_NAME, ANDROID_VERSION, PHONE_MODEL, STACK_TRACE, LOGCAT}
)
public class App extends Application {
    private static App INSTANCE = new App();
    private SharedPreferences preferences;
    private SharedPreferences preferencesPatch;
    Locale locale;
    String lang;

    public App() {
        INSTANCE = this;
    }

    public static App ctx() {
        return INSTANCE;
    }

    public static Context getContext() {
        return ctx();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.w(Constants.TAG, "\nINCORRECT SIGN :: \n" + StringWrapper.b("blah") + "\n");
        Log.w(Constants.TAG, "\nCORRECT SIGN ::\n" + StringWrapper.b(StringWrapper.gs()) + "\n");
        ACRA.init(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        // Изменение языка
        // TODO: Переделать под новый, не deprecated api [02.08.2017]
        Configuration config = getResources().getConfiguration();
        lang = Preferences.getDefaultLanguage();
        if (lang.equals("default"))
            lang = config.locale.getLanguage();
        locale = new Locale(lang);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config = getResources().getConfiguration();
        locale = new Locale(lang);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, null);
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return preferences;
    }

    public SharedPreferences getPatchPreferences() {
        if (preferencesPatch == null) {
            preferencesPatch = getSharedPreferences("patch_prefs", MODE_PRIVATE);
        }
        return preferencesPatch;
    }
}

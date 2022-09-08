package ru.SnowVolf.pcompiler;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.yandex.metrica.YandexMetrica;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import io.reactivex.annotations.NonNull;
import io.reactivex.plugins.RxJavaPlugins;
import ru.SnowVolf.girl.reactive.SimpleObservable;
import ru.SnowVolf.pcompiler.util.Constants;
import ru.SnowVolf.pcompiler.util.LocaleGirl;
import ru.SnowVolf.pcompiler.util.RuntimeUtil;
import ru.SnowVolf.pcompiler.util.StringWrapper;


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
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.BRAND,
                ReportField.PHONE_MODEL,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT
        }
)
public class App extends Application {
    private static App INSTANCE = null;
    private SharedPreferences preferences;

    private SimpleObservable preferenceChangeObservables = new SimpleObservable();
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
        Log.e(Constants.INSTANCE.getTAG(), String.format("PREFERENCE CHANGED: %s", key));
        if (key == null) return;
        preferenceChangeObservables.notifyObservers(key);
    };

    public App() {
        INSTANCE = this;
    }

    public static App ctx() {
        if (INSTANCE == null){
            INSTANCE = new App();
        }
        return INSTANCE;
    }

    public static Context getContext() {
        return ctx();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(throwable -> {
            Log.d(Constants.INSTANCE.getTAG(), String.format("RxJavaPlugins errorHandler %s", throwable));
            throwable.printStackTrace();
        });
        if (StringWrapper.b("8zuv+ap22YnX6ohcFCYktA")) {
            ACRA.init(this);
        }
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        // Инициализация AppMetrica SDK
        YandexMetrica.activate(getApplicationContext(), "16a6ee34-5d01-47bf-80db-f891ec82be42");
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleGirl.INSTANCE.onAttach(base, LocaleGirl.INSTANCE.getDefaultLocale().getLanguage()));
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return preferences;
    }

    public void addPreferenceChangeObserver(Observer observer) {
        preferenceChangeObservables.addObserver(observer);
    }

    public void removePreferenceChangeObserver(Observer observer) {
        preferenceChangeObservables.deleteObserver(observer);
    }

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private List<Runnable> permissionCallbacks = new ArrayList<>();

    //PLS CALL THIS IN ALL ACTIVITIES
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                for (Runnable runnable : permissionCallbacks) {
                    try {
                        runnable.run();
                    } catch (Exception ignore) {
                    }
                }
                break;
            }
        }
        permissionCallbacks.clear();
    }

    public int getStatusBarHeight(){
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0){
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    @ColorInt
    public static int getColorFromAttr(@NonNull Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        if (context != null && context.getTheme().resolveAttribute(attr, typedValue, true))
            return typedValue.data;
        else
            return Color.WHITE;
    }

    @DrawableRes
    public static int getDrawableResAttr(Context context, @AttrRes int attr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        int attributeResourceId = a.getResourceId(0, 0);
        a.recycle();
        return attributeResourceId;
    }

    public void checkStoragePermission(Runnable runnable, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RuntimeUtil.REQUEST_EXTERNAL_STORAGE_ZIP);
                permissionCallbacks.add(runnable);
                return;
            }
        }
        runnable.run();
    }

    public static Activity getActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null)
                return null;

            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String injectString(@StringRes int resId){
        return getContext().getString(resId);
    }
}

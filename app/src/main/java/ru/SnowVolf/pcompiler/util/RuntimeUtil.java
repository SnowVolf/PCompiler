package ru.SnowVolf.pcompiler.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import ru.SnowVolf.pcompiler.App;

/**
 * Created by Snow Volf on 22.08.2017, 0:06
 */

public class RuntimeUtil {
    public static final int REQUEST_EXTERNAL_STORAGE_TEXT = 1;
    public static final int REQUEST_EXTERNAL_STORAGE_ZIP = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean isGranted(String permission){
        return ContextCompat.checkSelfPermission(App.getContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }
    @RequiresApi(value = 23)
    public static void storage(Activity activity, int request) {
        Log.i(Constants.INSTANCE.getTAG(), "RuntimePermission");
        // Check if we have write permission

        Log.i(Constants.INSTANCE.getTAG(), "Check : RuntimePermission");
        if (!isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // We don't have permission so prompt the user
            Log.i(Constants.INSTANCE.getTAG(), "RequestPermissions : RuntimePermission");
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, request);
        }
    }
}

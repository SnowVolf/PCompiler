package ru.SnowVolf.girl.utils;

import android.util.Log;
import android.view.View;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 28.09.2017, 21:12
 */

public class ViewUtils {
    public static void getViewState(View v){
        Log.i(Constants.TAG, "[" + getNormalDate(System.currentTimeMillis()) + "] " + v.getClass().getSimpleName() + " with id " + v.getId() + ", " + "is " + status(v));
    }

    private static String status(View v){
        switch (v.getVisibility()){
            case View.VISIBLE:{
                return "visible";
            }
            case View.INVISIBLE:{
                return "invisible";
            }
            case View.GONE:{
                return "gone";
            }
            default:
        }
        return "unknown (" + v.getVisibility() + ")\nSee https://developer.android.com/ for details";
    }

    private static String getNormalDate(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        return format.format(date);
    }
}

package ru.SnowVolf.pcompiler.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;

import org.acra.ACRA;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.util.Objects;

import ru.SnowVolf.pcompiler.App;

/**
 * Created by Snow Volf on 19.08.2017, 12:38
 */

public class StringWrapper {

    public static void saveToPrefs(String key, String value){
        App.ctx().getPatchPreferences().edit().putString(key, value).apply();
    }

    public static String readFromPrefs(String key){
        return App.ctx().getPatchPreferences().getString(key, "");
    }

    //Копирование текста в буфер обмена
    public static void copyToClipboard(String s){
        ClipboardManager clipboard = (ClipboardManager) App.ctx().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("JavaGirl", s);
        clipboard.setPrimaryClip(clip);
    }

    private static PackageManager pm;
    private static String name;
    private static String s;
    private static byte[] bs;
    private static Signature[] sa;

    @SuppressLint("PackageManagerGetSignatures")
    public static boolean b(String st){
        try {
            pm = App.ctx().getPackageManager();
            name = App.ctx().getPackageName();
            sa = pm.getPackageInfo(name, PackageManager.GET_SIGNATURES).signatures;
            //Log.w(Constants.TAG, "Signature[] :: " + Arrays.toString(sa));
            for (Signature a$a : sa) {
                //Log.w(Constants.TAG, "byte[] :: " + Arrays.toString(bs));
                bs = a$a.toByteArray();
                //Log.w(Constants.TAG, "new byte[] :: " + Arrays.toString(bs));
                bs = CertificateFactory.getInstance("X509").generateCertificate(
                        new ByteArrayInputStream(bs)).getEncoded();
                //Log.w(Constants.TAG, "new byte[] encoded :: " + Arrays.toString(bs));
                s = new String(Base64.encode(MessageDigest.getInstance("MD5").digest(bs), 19));
                //Log.w(Constants.TAG, "result string :: " + s);
                return Objects.equals(st, s) && Build.VERSION.SDK_INT > 21;
            }
        } catch (Exception e){
            ACRA.getErrorReporter().handleException(e);
        }
        return false;
    }

//    @SuppressLint("PackageManagerGetSignatures")
//    public static String gs(){
//        try {
//            pm = App.ctx().getPackageManager();
//            name = App.ctx().getPackageName();
//            sa = pm.getPackageInfo(name, PackageManager.GET_SIGNATURES).signatures;
//            Log.w(Constants.TAG, "Signature[] :: " + Arrays.toString(sa));
//            for (Signature aSa : sa) {
//                Log.w(Constants.TAG, "byte[] :: " + Arrays.toString(bs));
//                bs = aSa.toByteArray();
//                Log.w(Constants.TAG, "new byte[] :: " + Arrays.toString(bs));
//                bs = CertificateFactory.getInstance("X509").generateCertificate(
//                        new ByteArrayInputStream(bs)).getEncoded();
//                Log.w(Constants.TAG, "new byte[] encoded :: " + Arrays.toString(bs));
//                s = new String(Base64.encode(MessageDigest.getInstance("MD5").digest(bs), 19));
//                Log.w(Constants.TAG, "result string :: " + s);
//                return s;
//            }
//        } catch (Exception e){
//            ACRA.getErrorReporter().handleException(e);
//        }
//        return null;
//    }
}

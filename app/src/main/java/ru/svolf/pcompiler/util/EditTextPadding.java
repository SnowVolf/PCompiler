package ru.svolf.pcompiler.util;

import android.content.Context;

/**
 * Created by Snow Volf on 07.12.2017, 23:19
 */

public class EditTextPadding {

    public static int getPaddingWithoutLineNumbers(Context context) {
        return (int) PixelDipConverter.INSTANCE.convertDpToPixel(5, context);
    }

    public static int getPaddingBottom(Context context) {
        return (int) PixelDipConverter.INSTANCE.convertDpToPixel(true ? 50 : 0, context);
    }

    public static int getPaddingWithLineNumbers(Context context, float fontSize) {
        return (int) PixelDipConverter.INSTANCE.convertDpToPixel(fontSize * 2f, context);
    }

    public static int getPaddingTop(Context context) {
        return getPaddingWithoutLineNumbers(context);
    }
}


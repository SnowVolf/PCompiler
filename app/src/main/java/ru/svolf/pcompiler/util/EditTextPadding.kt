package ru.svolf.pcompiler.util

import android.content.Context
import ru.svolf.pcompiler.util.PixelDipConverter.convertDpToPixel
import ru.svolf.pcompiler.util.PixelDipConverter
import ru.svolf.pcompiler.util.EditTextPadding

/**
 * Created by Snow Volf on 07.12.2017, 23:19
 */
object EditTextPadding {
    fun getPaddingWithoutLineNumbers(context: Context?): Int {
        return convertDpToPixel(5f, context!!).toInt()
    }

    fun getPaddingBottom(context: Context?): Int {
        return convertDpToPixel(50f, context!!).toInt()
    }

    @JvmStatic
    fun getPaddingWithLineNumbers(context: Context?, fontSize: Float): Int {
        return convertDpToPixel(fontSize * 2f, context!!).toInt()
    }

    @JvmStatic
    fun getPaddingTop(context: Context?): Int {
        return getPaddingWithoutLineNumbers(context)
    }
}
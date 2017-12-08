package ru.SnowVolf.pcompiler.util

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * Created by Snow Volf on 07.12.2017, 23:18
 */

object PixelDipConverter {

    /**
     * This method convets dp unit to equivalent device specific value in pixels.
     *
     * @param dp      A value in dp(Device independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent Pixels equivalent to dp according to device
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * metrics.densityDpi / 160f
    }

    /**
     * This method converts device specific pixels to device independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into dp
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent db equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }
}

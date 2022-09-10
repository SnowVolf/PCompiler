package ru.svolf.girl.utils

import android.util.Log
import android.view.View
import ru.svolf.pcompiler.util.Constants

/**
 * Created by Snow Volf on 28.09.2017, 21:12
 */

object ViewUtils {
    fun getViewState(v: View) {
        Log.i(Constants.TAG, v.javaClass.simpleName + " with id " + v.id + ", " + "is " + status(v))
    }

    private fun status(v: View): String {
        when (v.visibility) {
            View.VISIBLE -> {
                return "visible"
            }
            View.INVISIBLE -> {
                return "invisible"
            }
            View.GONE -> {
                return "gone"
            }
        }
        return "unknown (${v.visibility}) See https://developer.android.com/ for details"
    }
}

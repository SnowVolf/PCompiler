package ru.SnowVolf.pcompiler.patch


import android.widget.Toast

import java.util.LinkedHashMap
import java.util.Locale

import ru.SnowVolf.pcompiler.App
import ru.SnowVolf.pcompiler.R

/**
 * Created by Snow Volf on 24.10.2017, 19:18
 */

class PatchArray internal constructor() : LinkedHashMap<String, String>() {
    private val mBuildListener: ReactiveBuilder.OnBuildListener

    init {
        mBuildListener = object : ReactiveBuilder.OnBuildListener {
            override fun onSuccess() {
                Toast.makeText(App.getContext(), R.string.message_saved, Toast.LENGTH_SHORT).show()
            }

            override fun onError(index: Int) {
                Toast.makeText(App.getContext(), java.lang.String.format(Locale.ENGLISH, App.injectString(R.string.index_of_bounds_error), index, size), Toast.LENGTH_LONG).show()
            }
        }
    }


    fun removeItemAt(tabTag: String) {
        try {
            remove(tabTag)
        } catch (ignored: PatchException) {}
    }

    /**
     * Set item at the [LinkedHashMap.put]
     */
    @Deprecated("use {@link #setItemAt(String, ReactiveBuilder)} instead.")
    fun setItemAt(tabTag: String, s: String) {
        try {
            put(tabTag, s)
            mBuildListener.onSuccess()
        } catch (e: Exception) {
            mBuildListener.onError(-1)
            Toast.makeText(App.getContext(), e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun setItemAt(tabTag: String, builder: ReactiveBuilder) {
        try {
            put(tabTag, builder.toString())
            mBuildListener.onSuccess()
        } catch (e: Exception) {
            mBuildListener.onError(-1)
            Toast.makeText(App.getContext(), e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}
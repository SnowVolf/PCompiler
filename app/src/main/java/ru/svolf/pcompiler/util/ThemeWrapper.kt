package ru.svolf.pcompiler.util

import android.app.Activity

import ru.svolf.pcompiler.App
import ru.svolf.pcompiler.R

/**
 * Created by Snow Volf on 19.08.2017, 12:14
 */

object ThemeWrapper {

    val themeIndex: Int
        get() = Integer.parseInt(App.ctx().preferences.getString("ui.theme", ThemeWrapper.Theme.LIGHT.ordinal.toString()))

    val theme: Int
        get() {
            val theme: Int
            when (Theme.values()[themeIndex]) {
                ThemeWrapper.Theme.LIGHT -> theme = R.style.DialogLight
                ThemeWrapper.Theme.DARK -> theme = R.style.DialogDark
                ThemeWrapper.Theme.DARK_GLASS -> theme = R.style.DialogDark_Glass
            }
            return theme
        }

    val isLightTheme: Boolean
        get() = themeIndex == Theme.LIGHT.ordinal

    enum class Theme {
        LIGHT,
        DARK,
        DARK_GLASS
    }

    fun applyTheme(ctx: Activity) {
        val theme: Int
        when (Theme.values()[themeIndex]) {
            ThemeWrapper.Theme.LIGHT -> theme = R.style.AppTheme
            ThemeWrapper.Theme.DARK -> theme = R.style.AppTheme_Dark
            ThemeWrapper.Theme.DARK_GLASS -> theme = R.style.AppTheme_Dark_Glass
        }
        ctx.setTheme(theme)
    }
}

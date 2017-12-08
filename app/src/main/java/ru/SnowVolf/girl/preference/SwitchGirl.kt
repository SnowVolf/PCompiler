package ru.SnowVolf.girl.preference

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.preference.SwitchPreferenceCompat
import android.util.AttributeSet

/*
* Исправляет самопроизвольные переключения настроек в киткате.
* Пи*дец, да.
* */
class SwitchGirl : SwitchPreferenceCompat {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)
}

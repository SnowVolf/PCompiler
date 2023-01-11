package com.roacult.backdrop

import android.view.View

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
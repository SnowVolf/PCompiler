package ru.SnowVolf.pcompiler.tabs

/**
 * Created by Snow Volf on 21.10.2017, 15:34
 */

class TabConfiguration {
    var isAlone = false
    var isMenu = false
    var isUseCache = false
    var defaultTitle = ""

    override fun toString(): String {
        return "TabConfiguration {isAlone = $isAlone, isMenu = $isMenu, isUseCache = $isUseCache, defaultTitle = $defaultTitle}"
    }
}


package ru.SnowVolf.girl.reactive

import java.util.Observable

/**
 * Created by Snow Volf on 24.09.2017, 0:38
 */

class SimpleObservable : Observable() {
    @Synchronized override fun hasChanged(): Boolean {
        return true
    }
}

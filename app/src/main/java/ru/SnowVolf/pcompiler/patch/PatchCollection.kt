package ru.SnowVolf.pcompiler.patch


/**
 * Created by Snow Volf on 23.10.2017, 21:20
 */

object PatchCollection {
    private var collection: PatchArray? = null

    fun getCollection(): PatchArray {
        if (collection == null) {
            collection = PatchArray()
        }
        return collection!!
    }
}

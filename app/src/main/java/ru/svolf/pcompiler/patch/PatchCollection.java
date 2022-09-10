package ru.svolf.pcompiler.patch;

/**
 * Created by Snow Volf on 09.12.2017, 0:03
 */

public class PatchCollection {
    private static PatchArray collection = null;

    public static PatchArray getCollection(){
        if (collection == null)
            collection = new PatchArray();
        return collection;
    }
}

package ru.SnowVolf.pcompiler.patch;


/**
 * Created by Snow Volf on 23.10.2017, 21:20
 */

public class PatchCollection {
    private static PatchArray collection = null;

    public static PatchArray getCollection(){
        if (collection == null){
            collection = new PatchArray();
        }
        return collection;
    }
}

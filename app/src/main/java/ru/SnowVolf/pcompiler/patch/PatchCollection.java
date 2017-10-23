package ru.SnowVolf.pcompiler.patch;

import java.util.ArrayList;

/**
 * Created by Snow Volf on 23.10.2017, 21:20
 */

public class PatchCollection {
    private static ArrayList<String> collection = null;

    public static ArrayList<String> getCollection(){
        if (collection == null){
            collection = new ArrayList<>();
        }
        return collection;
    }
}

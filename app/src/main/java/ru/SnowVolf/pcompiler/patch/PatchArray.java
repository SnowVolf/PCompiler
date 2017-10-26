package ru.SnowVolf.pcompiler.patch;

import android.util.Log;

import java.util.ArrayList;

import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 24.10.2017, 19:18
 */

public class PatchArray extends ArrayList<String> {
    public void removeItemAt(int index){
        if (size() >= index){
            Log.i(Constants.TAG, "Trying to remove item at index: " + index);
            remove(index);
        }
    }
}

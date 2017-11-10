
package ru.SnowVolf.pcompiler.patch;


import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Locale;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;

/**
 * Created by Snow Volf on 24.10.2017, 19:18
 */

public class PatchArray extends LinkedHashMap<String, String> {
    private ReactiveBuilder.OnBuildListener mBuildListener;

    PatchArray() {
        mBuildListener = new ReactiveBuilder.OnBuildListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(App.getContext(), R.string.message_saved, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int index) {
                Toast.makeText(App.getContext(), String.format(Locale.ENGLISH, App.injectString(R.string.index_of_bounds_error), index, size()), Toast.LENGTH_LONG).show();
            }
        };
    }


    public void removeItemAt(String tabTag) {
        try {
            remove(tabTag);
        } catch (PatchException ignored) {
        }
    }

    /**
     * Set item at the {@link LinkedHashMap#put(Object, Object)}
     * @deprecated use {@link #setItemAt(String, ReactiveBuilder)} instead.
     */
    @Deprecated
    public void setItemAt(String tabTag, String s) {
        try {
            put(tabTag, s);
            mBuildListener.onSuccess();
        } catch (Exception e) {
            mBuildListener.onError(-1);
            Toast.makeText(App.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void setItemAt(String tabTag, ReactiveBuilder builder) {
        try {
            put(tabTag, builder.toString());
            mBuildListener.onSuccess();
        } catch (Exception e) {
            mBuildListener.onError(-1);
            Toast.makeText(App.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
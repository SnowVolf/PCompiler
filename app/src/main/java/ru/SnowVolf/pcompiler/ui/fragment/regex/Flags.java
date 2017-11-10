package ru.SnowVolf.pcompiler.ui.fragment.regex;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Snow Volf on 21.08.2017, 13:12
 */

public class Flags {
    private ArrayList<Boolean> mData = new ArrayList<>();
    private CharSequence[] mOptions = new CharSequence[]{"Case insensitive [i]", "Multiline [m]", "Comments [x]", "Dotall [s]", "Literal [l]", "Unicode Case [u]", "Unix Lines [d]"};

    public Flags(){
        for (int i = 0; i < mOptions.length; i++){
            mData.add(i, false);
        }
    }

    CharSequence[] getOptions(){
        return mOptions;
    }

    public void add(int id){
        mData.set(id, true);
    }

    void remove(int id){
        mData.set(id, false);
    }

    boolean[] getSelectedBooleans(){
        boolean[] data = new boolean[mOptions.length];
        for (int i = 0; i < mData.size(); i++){
            data[i] = mData.get(i);
        }
        return data;
    }
    /**
     * Чтобы не забыть
     * CANON_EQ (no effect on Android) == 128
     * CASE_INSENSITIVE == 2
     * COMMENTS == 4
     * DOTALL == 32
     * LITERAL == 16
     * MULTILINE == 8
     * UNICODE_CASE == 64
     * UNICODE_CHARACTER_CLASS (no effect on Android) == 256
     * UNIX_LINES == 1
     *
     * все константы можно посмотреть на developer.android.com
     */

    int getFlags(){
        int data = 0;
        if (mData.get(0)){
            data = Pattern.CASE_INSENSITIVE;
        }
        if (mData.get(1)){
            return data | Pattern.MULTILINE;
        }
        if (mData.get(2)){
            return data | Pattern.COMMENTS;
        }
        if (mData.get(3)){
            return data | Pattern.DOTALL;
        }
        if (mData.get(4)){
            return data | Pattern.LITERAL;
        }
        if (mData.get(5)){
            return data | Pattern.UNICODE_CASE;
        }
        if (mData.get(6)){
            return data | Pattern.UNIX_LINES;
        }
        return data;
    }

    String getFlagString(){
        String data = "/";
        if (mData.get(0)){
            data += "i";
        }
        if (mData.get(1)){
            data += "m";
        }
        if (mData.get(2)){
            data += "x";
        }
        if (mData.get(3)){
            data += "s";
        }
        if (mData.get(4)){
            data += "l";
        }
        if (mData.get(5)){
            data += "u";
        }
        if (mData.get(6)){
            data += "d";
        }
        if (data.equals("/")){
            return "Flags";
        }
        return data;
    }
}

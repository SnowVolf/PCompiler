package ru.svolf.pcompiler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.svolf.pcompiler.App;

/**
 * Created by Snow Volf on 20.08.2017, 19:08
 */

public class StrF {

    public static StringBuilder parseText(String fileUrl){
        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(App.ctx().getAssets().open(fileUrl), "UTF-8"));
            String line;
            while ((line = br1.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

}

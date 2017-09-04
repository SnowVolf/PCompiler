package ru.SnowVolf.pcompiler.settings;

import android.os.Environment;

import ru.SnowVolf.pcompiler.App;

/**
 * Created by Snow Volf on 20.08.2017, 1:21
 */

public class Preferences {
    public static String getDefaultLanguage(){
        return App.ctx().getPreferences().getString("sys.language", "default");
    }

    public static int getTabIndex() {
        return Integer.parseInt(App.ctx().getPreferences().getString("tab.sort", String.valueOf("1")));
    }

    public static boolean isTwiceBackAllowed(){
        return App.ctx().getPreferences().getBoolean("interaction.back", true);
    }

    public static void setPatchEngineVer(String s){
        App.ctx().getPreferences().edit().putString("preset.engine_ver", s).apply();
    }

    public static String getPatchEngineVer(){
        return App.ctx().getPreferences().getString("preset.engine_ver", "2");
    }

    public static void setPatchOutput(String s){
        App.ctx().getPreferences().edit().putString("preset.output", s).apply();
    }

    public static String getPatchOutput(){
        return App.ctx().getPreferences().getString("preset.output",
                Environment.getExternalStorageDirectory().getPath() + "/ApkEditor/PCompiler/");
    }

    public static void setPatchAuthor(String s){
        App.ctx().getPreferences().edit().putString("preset.author", s).apply();
    }

    public static String getPatchAuthor(){
        return App.ctx().getPreferences().getString("preset.author", "Snow Volf");
    }

    public static void setArchiveComment(String s){
        App.ctx().getPreferences().edit().putString("preset.archive_comment", s).apply();
    }

    public static String getArchiveComment(){
        return App.ctx().getPreferences().getString("preset.archive_comment", "Created by PCompiler");
    }

    public static boolean isEscapeFindAllowed(){
        return App.ctx().getPreferences().getBoolean("other.escape_find", false);
    }

    public static boolean isForceRegexpAllowed(){
        return App.ctx().getPreferences().getBoolean("other.regexp", true);
    }

    public static boolean isArtaSyntaxAllowed(){
        return App.ctx().getPreferences().getBoolean("ui.arta", false);
    }
}

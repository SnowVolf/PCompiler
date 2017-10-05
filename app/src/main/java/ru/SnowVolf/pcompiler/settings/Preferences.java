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

    public static String getPatchEngineVer(){
        return App.ctx().getPreferences().getString("preset.engine_ver", "2");
    }

    public static String getPatchOutput(){
        return App.ctx().getPreferences().getString("preset.output",
                Environment.getExternalStorageDirectory().getPath() + "/ApkEditor/PCompiler/");
    }

    public static String getPatchAuthor(){
        return App.ctx().getPreferences().getString("preset.author", "Snow Volf");
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

    public static String getMimeType(){
        return App.ctx().getPreferences().getString("preset.mime_type", "file/*");
    }

    public static boolean isMonospaceFontAllowed(){
        return App.ctx().getPreferences().getBoolean("ui.font_monospace", true);
    }

    public static int getFontSize(){
        int size = App.ctx().getPreferences().getInt("ui.font_size", 16);
        size = Math.max(Math.min(size, 64), 8);
        return size;
    }

    public static void setFontSize(int size){
        App.ctx().getPreferences().edit().putInt("ui.font_size", size).apply();
    }

    public static void saveString(String key, String value){
        App.ctx().getPatchPreferences().edit().putString(key, value).apply();
    }

    public static String readString(String key){
        return App.ctx().getPatchPreferences().getString(key, "");
    }
}

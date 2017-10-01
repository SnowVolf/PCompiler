package ru.SnowVolf.pcompiler.patch;

import android.widget.EditText;

import ru.SnowVolf.pcompiler.settings.Preferences;

/**
 * Created by Snow Volf on 22.08.2017, 22:12
 */

public class PatchBuilder {
    public static String insertAboutTag(EditText editText, String tag){
        if (!editText.getText().toString().isEmpty()) {
            return "[" + tag.toUpperCase() + "]" + System.lineSeparator() + editText.getText().toString()
                    + System.lineSeparator();
        }
        return "";
    }

    public static String insertStartTag(String tag){
        return System.lineSeparator() + "[" + tag.toUpperCase() +"]" + System.lineSeparator();
    }

    public static String insertEndTag(String tag){
        return "[/" + tag.toUpperCase() +"]" + System.lineSeparator();
    }

    public static String insertTag(EditText editText, String tag){
        if (!editText.getText().toString().isEmpty()) {
            return tag.toUpperCase() + ":" + System.lineSeparator() + editText.getText().toString()
                    + System.lineSeparator();
        }
        return "";
    }

    public static String insertMatchTag(EditText editText){
        if (!editText.getText().toString().isEmpty()){
            return "MATCH:" + System.lineSeparator() + escapeFind(editText.getText().toString())
                    + System.lineSeparator();
        }
        return "";
    }

    public static String insertReplaceTag(EditText editText){
        if (!editText.getText().toString().isEmpty()){
            return "REPLACE:" + System.lineSeparator() + editText.getText().toString()
                    + System.lineSeparator();
        }
        return "REPLACE:" + System.lineSeparator();
    }

    public static String regexTrue(boolean zed){
        if (zed){
            return "REGEX:" + System.lineSeparator() + "true" + System.lineSeparator();
        }
        return "";
    }

    public static String rootFolderTrue(boolean zed){
        if (zed){
            return "EXTRACT:" + System.lineSeparator() + "true" + System.lineSeparator();
        }
        return "";
    }

    public static String escapeComment(String comment){
        if (!comment.isEmpty()) {
            comment = "# " + comment.replace("\n", "\n# ");
            return System.lineSeparator() + comment + System.lineSeparator();
        }
        return "";
    }
//[  \t]*\\R[  \t]*
    private static String escapeFind(String find){
        if (Preferences.isEscapeFindAllowed()) {
            find = find
                    .replace("\n", "\\n")
                    .replace("    .", "    \\.")
                    .replaceAll(RegexPattern.REGISTERS, "([pv]\\\\d+)")
                    .replace("{", "\\{")
                    .replace("}", "\\}")
                    .replace("$", "\\$")
                    .replace(".method", "\\.method")
                    .replace(".end method", "\\.end method")
                    .replace(System.lineSeparator(), "\\n    ")
            .replaceAll(RegexPattern.EIGHT_SPACES, "    ");
        }
        return find;
    }
}

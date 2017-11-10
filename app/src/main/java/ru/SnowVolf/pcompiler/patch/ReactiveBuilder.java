package ru.SnowVolf.pcompiler.patch;

import android.widget.EditText;

import ru.SnowVolf.pcompiler.settings.Preferences;

/**
 * Created by Snow Volf on 07.11.2017, 15:06
 */

public class ReactiveBuilder {
    private StringBuilder mBuilder;

    public ReactiveBuilder(){
        mBuilder = new StringBuilder();
    }

    // Generate a full patch string
    public static StringBuilder build(){
        StringBuilder str = new StringBuilder();
        for (String s: PatchCollection.getCollection().values()) {
            str.append(s);
        }
        return str;
    }

    // Create about tag section
    public ReactiveBuilder insertAboutTag(EditText editText, String tag){
        if (!editText.getText().toString().isEmpty()) {
            mBuilder.append("[")
                    .append(tag.toUpperCase())
                    .append("]")
                    .append(System.lineSeparator())
                    .append(editText.getText().toString())
                    .append(System.lineSeparator());
        }
        return this;
    }
    // Insert [START] tag
    public ReactiveBuilder insertStartTag(String tag){
        mBuilder.append(System.lineSeparator())
                .append("[")
                .append(tag.toUpperCase())
                .append("]")
                .append(System.lineSeparator());
        return this;
    }

    // Insert [/END] tag
    public ReactiveBuilder insertEndTag(String tag){
        mBuilder.append("[/")
                .append(tag.toUpperCase())
                .append("]")
                .append(System.lineSeparator());
        return this;
    }

    // Insert SUB_TAG:
    public ReactiveBuilder insertTag(EditText editText, String tag){
        if (!editText.getText().toString().isEmpty()) {
            mBuilder.append(tag.toUpperCase())
                    .append(":")
                    .append(System.lineSeparator())
                    .append(editText.getText().toString())
                    .append(System.lineSeparator());
        }
        return this;
    }

    // Special version of #insertTag needed for match replace purposes
    public ReactiveBuilder insertMatchTag(EditText editText){
        if (!editText.getText().toString().isEmpty()){
            mBuilder.append("MATCH:")
                    .append(System.lineSeparator())
                    .append(escapeFind(editText.getText().toString()))
                    .append(System.lineSeparator());
        }
        return this;
    }

    // Special version of #insertTag needed for match replace purposes
    public ReactiveBuilder insertReplaceTag(EditText editText){
        if (!editText.getText().toString().isEmpty()){
            mBuilder.append("REPLACE:")
                    .append(System.lineSeparator())
                    .append(editText.getText().toString())
                    .append(System.lineSeparator());
        } else {
            mBuilder.append("REPLACE:")
                    .append(System.lineSeparator());
        }
        return this;
    }

    // If true, insert an
    // REGEX:
    // true
    public ReactiveBuilder regexTrue(boolean zed){
        if (zed){
            mBuilder.append("REGEX:")
                    .append(System.lineSeparator())
                    .append("true")
                    .append(System.lineSeparator());
        }
        return this;
    }

    // If true, insert an
    // EXTRACT:
    // true
    public ReactiveBuilder rootFolderTrue(boolean zed){
        if (zed){
            mBuilder.append("EXTRACT:")
                    .append(System.lineSeparator())
                    .append("true")
                    .append(System.lineSeparator());
        }
        return this;
    }

    // Escape all comment strings with '# '
    public ReactiveBuilder escapeComment(String comment){
        if (!comment.isEmpty()) {
            comment = "# " + comment.replace("\n", "\n# ");
            mBuilder.append(System.lineSeparator())
                    .append(comment)
                    .append(System.lineSeparator());
        }
        return this;
    }

    // Useful regex (detect 2 and more linebreak in text)
    // [  \t]*\\R[  \t]*

    // Escape most common sub sequences with its escaped equivalents
    private ReactiveBuilder escapeFind(String find){
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
                    .replace(".prologue", "\\.prologue")
                    .replace(".line ", "\\.line ")
                    .replace(System.lineSeparator(), "\\n    ")
                    .replaceAll(RegexPattern.EIGHT_SPACES, "    ");
        }
        mBuilder.append(find);
        return this;
    }

    @Override
    public String toString() {
        return mBuilder.toString();
    }

    // See usages of this interface
    public interface OnBuildListener{
        void onSuccess();
        void onError(int index);
    }
}

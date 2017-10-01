package ru.SnowVolf.pcompiler.patch;

import java.util.regex.Pattern;

/**
 * Created by Snow Volf on 26.08.2017, 15:05
 */

public class RegexPattern {
    public static Pattern COMMON_SYMBOLS = Pattern.compile("\\[|\\]|\\[/|\\.|\\{|\\}|true|false");
    public static Pattern ATTRIBUTE = Pattern.compile("MIN_ENGINE_VER|AUTHOR|PACKAGE|MATCH_GOTO|MATCH_ASSIGN|MATCH_REPLACE|GOTO|ADD_FILES|DUMMY" +
            "REMOVE_FILES|MERGE|DUMMY");
    public static Pattern SUB_ATTRIBUTE = Pattern.compile("REGEX:|MATCH:|REPLACE:|TARGET:|MERGE:|SOURCE:|EXTRACT:|NAME:");
    public static Pattern STRING = Pattern.compile("(?:'[^'\\\\\\n]*(?:\\\\.[^'\\\\\\n]*)*')|(?:\"[^\"\\\\\\n]*(?:\\\\.[^\"\\\\\\n]*)*\")");
    public static Pattern OPERATOR = Pattern.compile("\\([^)]+\\)|\\$", Pattern.CASE_INSENSITIVE);
    public static Pattern NUMBERS = Pattern.compile("(?<!-|/|\\$)-?\\b(?:0(X|x)[0-9a-zA-Z]+(?:t|T|s|S|l|L)?|\\d+(?:\\.\\d+)?(?:f|F|t|T|s|S|l|L)?)\\b", Pattern.CASE_INSENSITIVE);
    static String REGISTERS = "[pv]\\d+";
    static String EIGHT_SPACES = "(\\s){8}";
}

package ru.svolf.pcompiler.patch

import java.util.regex.Pattern

/**
 * Created by Snow Volf on 26.08.2017, 15:05
 */

object RegexPattern {
    var COMMON_SYMBOLS = Pattern.compile("\\[|\\]|\\[/|\\*|\\{|\\}|true|false")
    var ATTRIBUTE = Pattern.compile("MIN_ENGINE_VER|AUTHOR|PACKAGE|MATCH_GOTO|MATCH_ASSIGN|MATCH_REPLACE|GOTO|ADD_FILES|" + "ADD_FILES|REMOVE_FILES|MERGE|DUMMY|APPLICATION|LAUNCHER_ACTIVTIES|ACTIVTIES|GROUP\\d+")
    var SUB_ATTRIBUTE = Pattern.compile("REGEX:|MATCH:|REPLACE:|GOTO:|TARGET:|ASSIGN:|MERGE:|SOURCE:|EXTRACT:|NAME:")
    var STRING = Pattern.compile("(?:'[^'\\\\\\n]*(?:\\\\.[^'\\\\\\n]*)*')|(?:\"[^\"\\\\\\n]*(?:\\\\.[^\"\\\\\\n]*)*\")")
    var OPERATOR = Pattern.compile("\\([^)]+\\)|\\$", Pattern.CASE_INSENSITIVE)
    var NUMBERS = Pattern.compile("(?<!-|/|\\$)-?\\b(?:0(X|x)[0-9a-zA-Z]+(?:t|T|s|S|l|L)?|\\d+(?:\\.\\d+)?(?:f|F|t|T|s|S|l|L)?)\\b", Pattern.CASE_INSENSITIVE)
    var REGISTERS = "[pv]\\d+"
    var EIGHT_SPACES = "(\\s){8}"
}

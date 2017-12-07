package ru.SnowVolf.pcompiler.patch

import java.util.regex.Pattern

/**
 * Created by Snow Volf on 26.08.2017, 15:05
 */

object RegexPattern {
    val COMMON_SYMBOLS = Pattern.compile("\\[|]|\\[/|\\*|\\{|}|true|false")
    val ATTRIBUTE = Pattern.compile("MIN_ENGINE_VER|AUTHOR|PACKAGE|MATCH_GOTO|MATCH_ASSIGN|MATCH_REPLACE|GOTO|ADD_FILES|" + "ADD_FILES|REMOVE_FILES|MERGE|DUMMY|APPLICATION|LAUNCHER_ACTIVTIES|ACTIVTIES|GROUP\\d+")
    val SUB_ATTRIBUTE = Pattern.compile("REGEX:|MATCH:|REPLACE:|GOTO:|TARGET:|ASSIGN:|MERGE:|SOURCE:|EXTRACT:|NAME:")
    val STRING = Pattern.compile("(?:'[^'\\\\\\n]*(?:\\\\.[^'\\\\\\n]*)*')|(?:\"[^\"\\\\\\n]*(?:\\\\.[^\"\\\\\\n]*)*\")")
    val OPERATOR = Pattern.compile("\\([^)]+\\)|\\$", Pattern.CASE_INSENSITIVE)
    val NUMBERS = Pattern.compile("(?<![-/$])-?\\b(?:0([Xx])[0-9a-zA-Z]+(?:[tTsSlL])?|\\d+(?:\\.\\d+)?(?:[fFtTsSlL])?)\\b", Pattern.CASE_INSENSITIVE)
    val REGISTERS = "[pv]\\d+"
    val EIGHT_SPACES = "(\\s){8}"
}

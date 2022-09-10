package ru.svolf.pcompiler.settings

import android.os.Environment
import ru.svolf.pcompiler.App
import ru.svolf.pcompiler.tabs.TabFragment
import ru.svolf.pcompiler.ui.fragment.patch.*
import ru.svolf.pcompiler.ui.fragment.patch.match.AssignFragment
import ru.svolf.pcompiler.ui.fragment.patch.match.MGotoFragment
import ru.svolf.pcompiler.ui.fragment.patch.match.ReplaceFragment

/**
 * Created by Snow Volf on 20.08.2017, 1:21
 */

object Preferences {

    val tabIndex: Int
        get() = Integer.parseInt(App.ctx().preferences.getString("tab.sort", "0".toString()))

    val isTwiceBackAllowed: Boolean
        get() = App.ctx().preferences.getBoolean("interaction.back", true)

    val patchEngineVer: String?
        get() = App.ctx().preferences.getString("preset.engine_ver", "2")

    val patchOutput: String?
        get() = App.ctx().preferences.getString("preset.output",
                Environment.getExternalStorageDirectory().path + "/ApkEditor/PCompiler/")

    val patchAuthor: String?
        get() = App.ctx().preferences.getString("preset.author", "Snow Volf")

    val archiveComment: String?
        get() = App.ctx().preferences.getString("preset.archive_comment", "Created by PCompiler")

    val isEscapeFindAllowed: Boolean
        get() = App.ctx().preferences.getBoolean("other.escape_find", false)

    val isForceRegexpAllowed: Boolean
        get() = App.ctx().preferences.getBoolean("other.regexp", true)

    val isArtaSyntaxAllowed: Boolean
        get() = App.ctx().preferences.getBoolean("ui.arta", false)

    val mimeType: String?
        get() = App.ctx().preferences.getString("preset.mime_type", "file/*")

    val isMonospaceFontAllowed: Boolean
        get() = App.ctx().preferences.getBoolean("ui.font_monospace", true)

    var fontSize: Int
        get() {
            var size = App.ctx().preferences.getInt("ui.font_size", 16)
            size = Math.max(Math.min(size, 64), 8)
            return size
        }
        set(size) = App.ctx().preferences.edit().putInt("ui.font_size", size).apply()

    val isHelpShowed: Boolean
        get() = App.ctx().preferences.getBoolean("help_first_run", false)

    val startupTab: TabFragment
        get() {
            when (tabIndex) {
                0 -> {
                    return ReplaceFragment()
                }
                1 -> {
                    return MGotoFragment()
                }
                2 -> {
                    return AssignFragment()
                }
                3 -> {
                    return GotoFragment()
                }
                4 -> {
                    return AddFilesFragment()
                }
                5 -> {
                    return RemoveFilesFragment()
                }
                6 -> {
                    return MergeFragment()
                }
                7 -> {
                    return DummyFragment()
                }
                else -> return ReplaceFragment()
            }
        }

    fun setHelpShowed() {
        App.ctx().preferences.edit().putBoolean("help_first_run", true).apply()
    }
}

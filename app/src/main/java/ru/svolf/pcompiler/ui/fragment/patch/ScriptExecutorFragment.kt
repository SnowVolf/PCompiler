package ru.svolf.pcompiler.ui.fragment.patch


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.svolf.girl.annotation.Experimental
import ru.svolf.pcompiler.App
import ru.svolf.pcompiler.R
import ru.svolf.pcompiler.databinding.FragmentScriptExecutorBinding
import ru.svolf.pcompiler.patch.PatchCollection
import ru.svolf.pcompiler.patch.ReactiveBuilder
import ru.svolf.pcompiler.tabs.TabFragment
import ru.svolf.pcompiler.ui.activity.TabbedActivity
import ru.svolf.pcompiler.util.Constants
import java.io.File

class ScriptExecutorFragment : TabFragment() {
    lateinit var binding: FragmentScriptExecutorBinding
    private val REQUEST_ADD = 28

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentScriptExecutorBinding.inflate(inflater)
        baseInflateBinding(binding)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabTitle = getString(R.string.tab_executor)
        title = getString(R.string.tab_executor)
        subtitle = getString(R.string.subtitle_tab_executor)
        binding.drawerHeaderNick.text = String.format(getString(R.string.title_list_of_dex), TabbedActivity.extraDex?.size)
        binding.buttonBar.buttonSave.setOnClickListener { view ->
            val execPart: ReactiveBuilder

            execPart = ReactiveBuilder()
                .escapeComment(binding.fieldComment.text.toString())
                .insertStartTag("execute_dex")
                .insertTag(binding.fieldScript, "script")
                .smaliTrue(binding.checkboxSmali.isChecked)
                .insertTag(binding.fieldMainClass, "main_class")
                .insertTag(binding.fieldEntrance, "entrance")
                .insertTag(binding.fieldParam, "param")
                .insertEndTag("execute_dex")

            PatchCollection.getCollection().setItemAt(tag, execPart)
            tabActivity.updateTabList()
        }
        binding.buttonBar.buttonClear.setOnClickListener { view ->
            binding.fieldScript.clear()
            binding.fieldComment.clear()
            binding.fieldEntrance.clear()
            binding.fieldParam.clear()
            binding.fieldMainClass.clear()
            TabbedActivity.extraDex?.clear()
            PatchCollection.getCollection().removeItemAt(tag)
            App.ctx().preferences.edit().putString(Constants.KEY_EXTRA_DEXES, "").apply()
            tabActivity.updateTabList()
        }
        binding.add.setOnClickListener { view -> add() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_ADD -> {
                    val mLastSelectedFile = App.ctx().preferences.getString(Constants.KEY_EXTRA_DEXES, "")
                    if (mLastSelectedFile != data?.data?.path) {
                        App.ctx().preferences.edit().putString(Constants.KEY_EXTRA_DEXES, data?.data?.path).apply()
                        TabbedActivity.extraDex?.add(File(App.ctx().preferences.getString(Constants.KEY_EXTRA_DEXES, "")!!))
                        binding.drawerHeaderNick.text = String.format(getString(R.string.title_list_of_dex), TabbedActivity.extraDex?.size)
                        Toast.makeText(activity, data?.data?.path, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun add() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "file/dex"
        startActivityForResult(intent, REQUEST_ADD)
    }
    
    /**
     * Experimental feature
     */
    @Experimental
    private fun generateScript() {
        val runtime = Runtime.getRuntime()
        runtime.exec("javac -source 1.7 -target 1.7 " + TabbedActivity.extraDex!![0])
    }
}

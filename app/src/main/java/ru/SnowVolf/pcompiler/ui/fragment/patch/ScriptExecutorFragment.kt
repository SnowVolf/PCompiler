package ru.SnowVolf.pcompiler.ui.fragment.patch


import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle

import butterknife.BindView
import butterknife.ButterKnife
import ru.SnowVolf.girl.ui.GirlEditText
import ru.SnowVolf.pcompiler.App
import ru.SnowVolf.pcompiler.R
import ru.SnowVolf.pcompiler.patch.PatchCollection
import ru.SnowVolf.pcompiler.patch.ReactiveBuilder

import ru.SnowVolf.pcompiler.tabs.TabFragment
import ru.SnowVolf.pcompiler.ui.activity.TabbedActivity
import ru.SnowVolf.pcompiler.util.Constants

import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import ru.SnowVolf.girl.annotation.Experimental

import java.io.File

class ScriptExecutorFragment : TabFragment() {

    @BindView(R.id.field_comment) private var mFieldComment: GirlEditText? = null
    @BindView(R.id.field_script) private var mFieldScript: GirlEditText? = null
    @BindView(R.id.checkbox_smali) private var mCheckbox: CheckBox? = null
    @BindView(R.id.field_main_class) private var mFieldMainClass: GirlEditText? = null
    @BindView(R.id.field_entrance) private var mFieldEntrance: GirlEditText? = null
    @BindView(R.id.field_param) private var mFieldParam: GirlEditText? = null
    @BindView(R.id.button_save) internal var buttonSave: Button? = null
    @BindView(R.id.button_clear) internal var buttonClear: Button? = null
    @BindView(R.id.add) private var mButtonAdd: ImageButton? = null
    @BindView(R.id.drawer_header_nick) private var mFileCaption: AppCompatTextView? = null
    private val REQUEST_ADD = 28

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        baseInflateFragment(inflater, R.layout.fragment_script_executor)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        tabTitle = getString(R.string.tab_executor)
        title = getString(R.string.tab_executor)
        subtitle = getString(R.string.subtitle_tab_executor)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonSave!!.setOnClickListener { view ->
            val execPart: ReactiveBuilder

            execPart = ReactiveBuilder()
                    .escapeComment(mFieldComment!!.text.toString())
                    .insertStartTag("execute_dex")
                    .insertTag(mFieldScript, "script")
                    .smaliTrue(mCheckbox!!.isChecked)
                    .insertTag(mFieldMainClass, "main_class")
                    .insertTag(mFieldEntrance, "entrance")
                    .insertTag(mFieldParam, "param")
                    .insertEndTag("execute_dex")

            PatchCollection.getCollection().setItemAt(tag, execPart)
        }
        buttonClear!!.setOnClickListener { view ->
            mFieldComment!!.setText("")
            mFieldScript!!.setText("")
            mFieldMainClass!!.setText("")
            mFieldEntrance!!.setText("")
            mFieldParam!!.setText("")
            TabbedActivity.extraDex.clear()
            PatchCollection.getCollection().removeItemAt(tag)
            App.ctx().preferences.edit().putString(Constants.KEY_EXTRA_DEXES, "").apply()
        }
        mButtonAdd!!.setOnClickListener { view -> add() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_ADD -> {
                    val mLastSelectedFile = App.ctx().preferences.getString(Constants.KEY_EXTRA_DEXES, "")
                    if (mLastSelectedFile != data!!.data!!.path) {
                        App.ctx().preferences.edit().putString(Constants.KEY_EXTRA_DEXES, data.data!!.path).apply()
                        TabbedActivity.extraDex.add(File(App.ctx().preferences.getString(Constants.KEY_EXTRA_DEXES, "")!!))
                        mFileCaption!!.text = String.format(getString(R.string.title_list_of_dex), TabbedActivity.extraDex.size)
                        Toast.makeText(activity, data.data!!.path, Toast.LENGTH_LONG).show()
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
        runtime.exec("javac -source 1.7 -target 1.7 " + TabbedActivity.extraDex[0])
    }
}

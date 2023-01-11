package ru.svolf.pcompiler.ui.activity

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.roacult.backdrop.BackdropLayout
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import ru.svolf.girl.ui.CodeText
import ru.svolf.girl.ui.GirlToolbar
import ru.svolf.pcompiler.App
import ru.svolf.pcompiler.R
import ru.svolf.pcompiler.databinding.ActivityTabsBinding
import ru.svolf.pcompiler.patch.PatchCollection
import ru.svolf.pcompiler.patch.ReactiveBuilder
import ru.svolf.pcompiler.settings.Preferences.archiveComment
import ru.svolf.pcompiler.settings.Preferences.isTwiceBackAllowed
import ru.svolf.pcompiler.settings.Preferences.patchOutput
import ru.svolf.pcompiler.tabs.TabFragment
import ru.svolf.pcompiler.tabs.TabManager
import ru.svolf.pcompiler.ui.fragment.dialog.SweetInputDialog
import ru.svolf.pcompiler.ui.fragment.dialog.SweetListDialog
import ru.svolf.pcompiler.ui.fragment.other.FullEditorFragment
import ru.svolf.pcompiler.ui.widget.drawers.Drawers
import ru.svolf.pcompiler.util.Constants.KEY_EXTRA_FILES
import ru.svolf.pcompiler.util.Constants.TAG
import ru.svolf.pcompiler.util.RuntimeUtil
import ru.svolf.pcompiler.util.StringWrapper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class TabbedActivity : BaseActivity(), TabManager.TabListener {
    private val binding by lazy { ActivityTabsBinding.inflate(layoutInflater) }
    private var mFileName = "patch.txt"
    var drawers: Drawers? = null
        private set
    private var mBackDrop: BackdropLayout? = null
    val removeTabListener = View.OnClickListener { view: View? -> backHandler(true) }
    private var tabToolbar: GirlToolbar? = null

    init {
        TabManager.init(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        mBackDrop = binding.backDrop
        tabToolbar = binding.bottomSheet.bottomSheetBack.tabToolbar
        updateInsets()
        drawers = Drawers(this, mBackDrop)
        drawers!!.init(savedInstanceState)
        extra = ArrayList()
        extraDex = ArrayList()
        addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
               menuInflater.inflate(R.menu.menu_tabbed, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
               return when (menuItem.itemId){
                   R.id.action_settings -> {
                       showMenuDialog()
                       true
                   }
                 else -> false
               }
            }
        })
        tabToolbar?.setOnMenuItemClickListener {
             when(it.itemId) {
                R.id.action_save -> {
                    showSaveDialog()
                    true
                } else -> false
            }
        }
        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isTwiceBackAllowed) {
                    if (press_time + 2000 > System.currentTimeMillis()) {
                        finish()
                    } else {
                        Toast.makeText(this@TabbedActivity, R.string.message_press_back, Toast.LENGTH_SHORT).show()
                        press_time = System.currentTimeMillis()
                    }
                } else onBackPressedDispatcher.onBackPressed()
            }
        })
        requestPermissionR()
    }

    private fun showMenuDialog() {
        val dialog = SweetListDialog(this)
        dialog.setTitle(R.string.app_name)
        dialog.setItems(R.array.params_title)
        dialog.setAdapter()
        dialog.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> startActivity(Intent(this, RegexpActivity::class.java))
                1 -> startActivity(Intent(this, SettingsActivity::class.java))
                2 -> startActivity(Intent(this, AboutActivity::class.java))
            }
        }
        dialog.show()
    }

    @SuppressLint("WrongConstant")
    private fun updateInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            binding.toolbar.updatePadding(top = insets.getInsets(1).top)
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                RuntimeUtil.REQUEST_EXTERNAL_STORAGE_TEXT -> {
                    checkPermissionsText()
                }
                RuntimeUtil.REQUEST_EXTERNAL_STORAGE_ZIP -> {
                    checkPermissionsZip()
                }
            }
        }
    }

    fun showSaveDialog() {
        val saveDialog = SweetListDialog(this)
        saveDialog.setTitle(getString(R.string.title_save))
        saveDialog.setItems(R.array.save_items)
        saveDialog.setAdapter()
        saveDialog.setOnItemClickListener { adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
            when (i) {
                0 -> {
                    StringWrapper.copyToClipboard(ReactiveBuilder.build())
                    Toast.makeText(this@TabbedActivity, R.string.message_copied_to_clipboard, Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    showSaveNameDialog()
                }
                2 -> {
                    showSaveZipDialog()
                }
                3 -> {
                    val send = Intent(Intent.ACTION_SEND)
                    send.type = "text/plain"
                    send.putExtra(Intent.EXTRA_TEXT, ReactiveBuilder.build().toString())
                    startActivity(send)
                }
            }
        }
        saveDialog.show()
    }

    private fun requestPermissionR() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                    startActivityForResult(intent, 2296)
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivityForResult(intent, 2296)
                }
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this@TabbedActivity, arrayOf(WRITE_EXTERNAL_STORAGE), 577)
        }
    }

    private fun checkPermissionsText() {
        if (Build.VERSION.SDK_INT >= 23 && !RuntimeUtil.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            RuntimeUtil.storage(this, RuntimeUtil.REQUEST_EXTERNAL_STORAGE_TEXT)
        } else {
            writeToFile(ReactiveBuilder.build())
        }
    }

    private fun checkPermissionsZip() {
        if (Build.VERSION.SDK_INT >= 23 && !RuntimeUtil.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            RuntimeUtil.storage(this, RuntimeUtil.REQUEST_EXTERNAL_STORAGE_ZIP)
        } else {
            zipIo()
        }
    }

    private fun writeToFile(data: StringBuilder?) {
        var patchFile: File? = null
        try {
            val dir = File(patchOutput)
            if (!dir.exists() && !dir.isDirectory) {
                dir.mkdirs()
            }
            patchFile = File(dir, "$mFileName.txt")
            if (!patchFile.exists()) {
                patchFile.createNewFile()
                Toast.makeText(this, R.string.message_file_overwrite, Toast.LENGTH_SHORT).show()
            }
            val outputStream = FileOutputStream(patchFile)
            if (data != null && !data.toString().isEmpty()) {
                outputStream.write(data.toString().toByteArray())
            } else {
                Toast.makeText(this, R.string.message_patch_cannot_empty, Toast.LENGTH_SHORT).show()
            }
            outputStream.close()
            Toast.makeText(this, String.format(getString(R.string.message_saved_in_path), patchOutput, "$mFileName.txt"), Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun writeToTempFile(data: StringBuilder?): File? {
        var patchFile: File? = null
        try {
            patchFile = File(getExternalFilesDir(null), "patch.txt")
            if (!patchFile.exists()) {
                patchFile.createNewFile()
            }
            val outputStream = FileOutputStream(patchFile)
            if (data != null && !data.toString().isEmpty()) {
                outputStream.write(data.toString().toByteArray())
            } else {
                Toast.makeText(this, R.string.message_patch_cannot_empty, Toast.LENGTH_SHORT).show()
                return null
            }
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
        return patchFile
    }

    private fun showSaveNameDialog() {
        val dialog = SweetInputDialog(this)
        dialog.setPrefTitle(getString(R.string.title_filename))
        dialog.setPositive(getString(R.string.button_save)) { view: View? ->
            mFileName = dialog.inputString
            checkPermissionsText()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showFullViewDialog() {
        val dialog = BottomSheetDialog(this)
        @SuppressLint("InflateParams") val view = LayoutInflater.from(this).inflate(R.layout.dialog_full_view, null)
        val captionBar = view.findViewById<Toolbar>(R.id.caption_bar)
        val content = view.findViewById<CodeText>(R.id.content)
        val save = view.findViewById<Button>(R.id.button_save_patch)
        captionBar.inflateMenu(R.menu.menu_view_dialog)
        content.text = ReactiveBuilder.build()
        content.setUpdateDelay(App.ctx().preferences.getString("sys.delay", "2000")!!.toInt())
        captionBar.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_edit -> {
                    val fragment = FullEditorFragment.newInstance(ReactiveBuilder.build())
                    fragment.configuration.isAlone = true
                    TabManager.getInstance().add(fragment)
                    dialog.dismiss()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_reset -> {
                    content.text = ""
                    PatchCollection.getCollection().clear()
                    extra!!.clear()
                    extraDex!!.clear()
                    dialog.dismiss()
                    Toast.makeText(this, R.string.message_patch_cleared, Toast.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_full_view -> {
                    dialog.dismiss()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        save.setOnClickListener { v: View? -> showSaveDialog() }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showSaveZipDialog() {
        val dialog = SweetInputDialog(this)
        dialog.setPrefTitle(getString(R.string.title_zip_filename))
        dialog.setPositive(getString(R.string.button_save)) { view: View? ->
            mFileName = dialog.inputString
            if (mFileName != "patch") {
                checkPermissionsZip()
                dialog.dismiss()
            } else {
                Toast.makeText(App.getContext(), R.string.message_name_reserved, Toast.LENGTH_LONG).show()
            }
        }
        dialog.show()
    }

    private fun zipIo() {
        val dir = File(patchOutput)
        if (!dir.exists() && !dir.isDirectory) {
            dir.mkdirs()
        }
        val temp = writeToTempFile(ReactiveBuilder.build())
        try {
            val zipFile = ZipFile("$patchOutput$mFileName.zip")
            val parameters = ZipParameters()
            parameters.compressionMethod = CompressionMethod.DEFLATE
            parameters.compressionLevel = CompressionLevel.NO_COMPRESSION
            zipFile.addFile(temp, parameters)
            if (!App.ctx().preferences.getString(KEY_EXTRA_FILES, "")!!.isEmpty()) {
                try {
                    zipFile.addFiles(extra, parameters)
                    zipFile.addFiles(extraDex, parameters)
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            }
            zipFile.comment = archiveComment
            Toast.makeText(this, String.format(getString(R.string.message_saved_in_path_zip), zipFile.file.absolutePath), Toast.LENGTH_LONG).show()
        } catch (e: ZipException) {
            Log.e(TAG, e.message!!)
        }
    }

    fun updateTabList() {
        drawers!!.notifyTabsChanged()
    }

    fun hideKeyboard() {
        if (currentFocus != null) (this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    fun showKeyboard(view: View?) {
        if (currentFocus != null) (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(view, 0)
    }

    fun backHandler(fromToolbar: Boolean) {
        if (fromToolbar || !TabManager.getInstance().active.onBackPressed()) {
            hideKeyboard()
            TabManager.getInstance().remove(TabManager.getInstance().active)
            if (TabManager.getInstance().size < 1) {
                finish()
            }
        }
    }

    override fun onAddTab(fragment: TabFragment) {
        Log.d(TAG, "onAdd : $fragment")
    }

    override fun onRemoveTab(fragment: TabFragment) {
        Log.d(TAG, "onRemove : $fragment")
        //TabFragment.toolbar.setTabIndicatorValue(TabManager.getInstance().getSize() == 0 ? 2 : TabManager.getInstance().getSize());
    }

    override fun onSelectTab(fragment: TabFragment) {
        Log.d(TAG, "onSelect : $fragment")
    }

    override fun onChange() {
        updateTabList()
        tabToolbar!!.setTabIndicatorValue(if (TabManager.getInstance().size == 0) 2 else TabManager.getInstance().size)
    }

    companion object {
        @JvmField
        var extra: ArrayList<File>? = null
        var extraDex: ArrayList<File>? = null
        private var press_time = System.currentTimeMillis()
    }
}
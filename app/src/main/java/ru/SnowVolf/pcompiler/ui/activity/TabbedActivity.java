package ru.SnowVolf.pcompiler.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.acra.ACRA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.patch.RegexPattern;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.tabs.TabFragment;
import ru.SnowVolf.pcompiler.tabs.TabManager;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetContentDialog;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetInputDialog;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetListDialog;
import ru.SnowVolf.pcompiler.ui.widget.drawers.Drawers;
import ru.SnowVolf.pcompiler.util.Constants;
import ru.SnowVolf.pcompiler.util.LocaleGirl;
import ru.SnowVolf.pcompiler.util.RuntimeUtil;
import ru.SnowVolf.pcompiler.util.StringWrapper;
import ru.SnowVolf.pcompiler.util.ThemeWrapper;

public class TabbedActivity extends BaseActivity implements TabManager.TabListener {
    private String fileName = "patch.txt";
    private String lang = null;
    public static ArrayList<File> extra;
    private Drawers drawers;
    private final View.OnClickListener toggleListener = view -> drawers.toggleMenu();
    private final View.OnClickListener removeTabListener = view -> backHandler(true);

    public View.OnClickListener getToggleListener() {
        return toggleListener;
    }

    public View.OnClickListener getRemoveTabListener() {
        return removeTabListener;
    }

    public TabbedActivity() {
        TabManager.init(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar bottomBar = findViewById(R.id.bottom_bar);
        drawers = new Drawers(this, drawer);
        drawers.init(savedInstanceState);
        extra = new ArrayList<>();
        bottomBar.inflateMenu(R.menu.menu_chevron);
        bottomBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_open_full: {
                    showFullViewDialog();
                    return true;
                }
            }
            return false;
        });
        if (!Preferences.isHelpShowed()){
            try {
                startActivity(new Intent(this, HelpActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private static long press_time = System.currentTimeMillis();
    public void onBackPressed() {
        if (Preferences.isTwiceBackAllowed()) {
            if (press_time + 2000 > System.currentTimeMillis()) {
                finish();
            } else {
                Toast.makeText(this, R.string.message_press_back, Toast.LENGTH_SHORT).show();
                press_time = System.currentTimeMillis();
            }
        } else super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode){
                case RuntimeUtil.REQUEST_EXTERNAL_STORAGE_TEXT:{
                    checkPermissionsText();
                    break;
                }
                case RuntimeUtil.REQUEST_EXTERNAL_STORAGE_ZIP:{
                    checkPermissionsZip();
                    break;
                }
            }
        }
    }

    //Активити возвращается в активное состояние
    @Override
    public void onResume() {
        super.onResume();
        drawers.setStatusBarHeight(App.ctx().getStatusBarHeight());
        // Языковые настройки
        if (lang == null){
            lang = LocaleGirl.getLanguage(this);
        }
        // Проверка не изменися ли язык
        if (!LocaleGirl.getLanguage(this).equals(lang)){
            //Context newContext = LocaleGirl.onAttach(this);
            SweetContentDialog dialog = new SweetContentDialog(this);
            dialog.setContentText(R.string.app_lang_changed);
            dialog.setPositive(android.R.string.ok, v -> {
                        Intent mStartActivity = new Intent(this, TabbedActivity.class);
                        int mIntentPendingId = 6;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(TabbedActivity.this, mIntentPendingId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager manager = (AlarmManager) TabbedActivity.this.getSystemService(Context.ALARM_SERVICE);
                        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    });
            dialog.setNegative(android.R.string.cancel, v -> dialog.dismiss());
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawers.destroy();
    }

    public void showSaveDialog() {
        SweetListDialog saveDialog = new SweetListDialog(this);
        saveDialog.setTitle(getString(R.string.title_save));
        saveDialog.setItems(R.array.save_items);
        saveDialog.setAdapter();
        saveDialog.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0: {
                    StringBuilder str = new StringBuilder();
                    for (String s: PatchCollection.getCollection().values()) {
                        str.append(s);
                    }
                    StringWrapper.copyToClipboard(str.toString());
                    Toast.makeText(TabbedActivity.this, R.string.message_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1:{
                    showSaveNameDialog();
                    break;
                }
                case 2:{
                    showSaveZipDialog();
                    break;
                }
                case 3:{
                    final Intent send = new Intent(Intent.ACTION_SEND);
                    send.setType("text/plain");
                    StringBuilder str = new StringBuilder();
                    for (String s: PatchCollection.getCollection().values()) {
                        str.append(s);
                    }
                    send.putExtra(Intent.EXTRA_TEXT, str.toString());

                    startActivity(send);
                    break;
                }
            }
        });
        saveDialog.show();
    }


    private void checkPermissionsText(){
        if (Build.VERSION.SDK_INT >= 23 && !RuntimeUtil.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            RuntimeUtil.storage(this, RuntimeUtil.REQUEST_EXTERNAL_STORAGE_TEXT);
        } else {
            StringBuilder str = new StringBuilder();
            for (String s: PatchCollection.getCollection().values()) {
                str.append(s);
            }
            writeToFile(str.toString());
        }
    }

    private void checkPermissionsZip(){
        if (Build.VERSION.SDK_INT >= 23 && !RuntimeUtil.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            RuntimeUtil.storage(this, RuntimeUtil.REQUEST_EXTERNAL_STORAGE_ZIP);
        } else {
            zipIo();
        }
    }

    private File writeToFile(String data){
        File patchFile = null;
        try {
            File dir = new File(Preferences.getPatchOutput());
            if (!dir.exists() && !dir.isDirectory()){
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            patchFile = new File(dir, fileName + ".txt");
            if (!patchFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                patchFile.createNewFile();
                Toast.makeText(this, R.string.message_file_overwrite, Toast.LENGTH_SHORT).show();
            }
                FileOutputStream outputStream = new FileOutputStream(patchFile);
                if (data != null && !data.isEmpty()){
                    outputStream.write(data.getBytes());
                } else {
                    Toast.makeText(this, R.string.message_patch_cannot_empty, Toast.LENGTH_SHORT).show();
                    return null;
                }
                outputStream.close();
                Toast.makeText(this, String.format(getString(R.string.message_saved_in_path), Preferences.getPatchOutput(), fileName + ".txt"), Toast.LENGTH_LONG).show();
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return patchFile;
    }

    private File writeToTempFile(String data){
        File patchFile = null;
        try {
            patchFile = new File(getExternalFilesDir(null), "patch.txt");
            if (!patchFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                patchFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(patchFile);
            if (data != null && !data.isEmpty()){
                outputStream.write(data.getBytes());
            } else {
                Toast.makeText(this, R.string.message_patch_cannot_empty, Toast.LENGTH_SHORT).show();
                return null;
            }
            outputStream.close();
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return patchFile;
    }

    private void showSaveNameDialog(){
        final SweetInputDialog dialog = new SweetInputDialog(this);
        dialog.setPrefTitle(getString(R.string.title_filename));
        dialog.setPositive(getString(R.string.button_save), view -> {
            fileName = dialog.getInputString();
            checkPermissionsText();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showFullViewDialog(){
        final BottomSheetDialog dialog = new BottomSheetDialog(this, ThemeWrapper.getTheme());
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(this).inflate(R.layout.dialog_full_view, null);
        final Toolbar captionBar = view.findViewById(R.id.caption_bar);
        final TextView content = view.findViewById(R.id.content);
        final Button save = view.findViewById(R.id.button_save_patch);
        captionBar.inflateMenu(R.menu.menu_view_dialog);
        captionBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.action_reset:{
                    content.setText("");
                    PatchCollection.getCollection().clear();
                    extra.clear();
                    Toast.makeText(this, R.string.message_patch_cleared, Toast.LENGTH_SHORT).show();
                    return true;
                }
                case R.id.action_full_view:{
                    dialog.dismiss();
                    return true;
                }
            }
            return false;
        });
        save.setOnClickListener(v -> showSaveDialog());
        Spannable spannable;
        if (Preferences.isMonospaceFontAllowed()) {
            final Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoMono-Regular.ttf");
            content.setTypeface(typeface);
        }
        content.setTextSize(Preferences.getFontSize());
        StringBuilder str = new StringBuilder();
        for (String s: PatchCollection.getCollection().values()) {
            str.append(s);
        }
        content.setText(str);
        spannable = new SpannableString(content.getText());
        Matcher matcherAttr = RegexPattern.ATTRIBUTE.matcher(content.getText());
        Matcher matcherSubAttr = RegexPattern.SUB_ATTRIBUTE.matcher(content.getText());
        Matcher matcherBraces = RegexPattern.COMMON_SYMBOLS.matcher(content.getText());
        Matcher matcherNumAttr = RegexPattern.OPERATOR.matcher(content.getText());
        Matcher matcherNum = RegexPattern.NUMBERS.matcher(content.getText());
        Matcher matcherString = RegexPattern.STRING.matcher(content.getText());

        while (matcherAttr.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_element) : ContextCompat.getColor(App.getContext(), R.color.syntax_element)), matcherAttr.start(), matcherAttr.end(), 33);
        }

        while (matcherSubAttr.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_sub_element) : ContextCompat.getColor(App.getContext(), R.color.syntax_sub_element)), matcherSubAttr.start(), matcherSubAttr.end(), 33);
        }

        while (matcherBraces.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_keyword) : ContextCompat.getColor(App.getContext(), R.color.syntax_keyword)), matcherBraces.start(), matcherBraces.end(), 33);
        }

        while (matcherNumAttr.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_num_attribute) : ContextCompat.getColor(App.getContext(), R.color.syntax_num_attribute)), matcherNumAttr.start(), matcherNumAttr.end(), 33);
        }

        while (matcherNum.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_num) : ContextCompat.getColor(App.getContext(), R.color.syntax_num)), matcherNum.start(), matcherNum.end(), 33);
        }

        while (matcherString.find()){
            spannable.setSpan(new ForegroundColorSpan(Preferences.isArtaSyntaxAllowed() ? ContextCompat.getColor(App.getContext(), R.color.syntax_arta_string) : ContextCompat.getColor(App.getContext(), R.color.syntax_string)), matcherString.start(), matcherString.end(), 33);
        }
        content.setText(spannable);
        dialog.setContentView(view);
        dialog.show();
    }
    
    private void showSaveZipDialog(){
        final SweetInputDialog dialog = new SweetInputDialog(this);
        dialog.setPrefTitle(getString(R.string.title_zip_filename));
        dialog.setPositive(getString(R.string.button_save), view -> {
            fileName = dialog.getInputString();
            if (!Objects.equals(fileName, "patch")){
                checkPermissionsZip();
                dialog.dismiss();
            } else {
                Toast.makeText(App.getContext(), R.string.message_name_reserved, Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    private void zipIo(){
        File dir = new File(Preferences.getPatchOutput());
        if (!dir.exists() && !dir.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        StringBuilder str = new StringBuilder();
        for (String s: PatchCollection.getCollection().values()) {
            str.append(s);
        }
        File temp = writeToTempFile(str.toString());

        try {
            ZipFile zipFile = new ZipFile(Preferences.getPatchOutput() + fileName + ".zip");
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(0);
            zipFile.addFile(temp, parameters);
            if (!Preferences.readString(Constants.KEY_EXTRA_FILES).isEmpty()) {
                try {
                    zipFile.addFiles(extra, parameters);
                } catch (Exception e){
                    ACRA.getErrorReporter().handleException(e);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            zipFile.setComment(Preferences.getArchiveComment());
            Toast.makeText(this, String.format(getString(R.string.message_saved_in_path_zip), zipFile.getFile().getAbsolutePath()), Toast.LENGTH_LONG).show();
        } catch (ZipException e){
            Log.e(Constants.TAG, e.getMessage());
            ACRA.getErrorReporter().handleException(e);
        }
    }

    public Drawers getDrawers() {
        return drawers;
    }

    public void updateTabList() {
        drawers.notifyTabsChanged();
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null)
            ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        if (getCurrentFocus() != null)
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .showSoftInput(view, 0);
    }

    public void backHandler(boolean fromToolbar) {
        if (fromToolbar || !TabManager.getInstance().getActive().onBackPressed()) {
            hideKeyboard();
            TabManager.getInstance().remove(TabManager.getInstance().getActive());
            if (TabManager.getInstance().getSize() < 1) {
                finish();
            }
        }
    }

    @Override
    public void onAddTab(TabFragment fragment) {
        Log.d(Constants.TAG, "onAdd : " + fragment);
    }

    @Override
    public void onRemoveTab(TabFragment fragment) {
        Log.d(Constants.TAG, "onRemove : " + fragment);
    }

    @Override
    public void onSelectTab(TabFragment fragment) {
        Log.d(Constants.TAG, "onSelect : " + fragment);
    }

    @Override
    public void onChange() {
        updateTabList();
    }
}

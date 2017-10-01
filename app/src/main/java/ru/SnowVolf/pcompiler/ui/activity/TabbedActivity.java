package ru.SnowVolf.pcompiler.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import ru.SnowVolf.pcompiler.adapter.ViewPagerAdapter;
import ru.SnowVolf.pcompiler.patch.RegexPattern;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetContentDialog;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetInputDialog;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetListDialog;
import ru.SnowVolf.pcompiler.ui.fragment.patch.AboutPatchFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.AddFilesFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.DummyFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.MergeFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.RemoveFilesFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.match.AssignFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.match.GotoFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.match.ReplaceFragment;
import ru.SnowVolf.pcompiler.util.Constants;
import ru.SnowVolf.pcompiler.util.LocaleGirl;
import ru.SnowVolf.pcompiler.util.RuntimeUtil;
import ru.SnowVolf.pcompiler.util.StringWrapper;
import ru.SnowVolf.pcompiler.util.ThemeWrapper;

public class TabbedActivity extends BaseActivity {
    ArrayList<String> patch;
    String fileName = "patch.txt";
    private String lang = null;
    ViewPagerAdapter adapter;
    public static ArrayList<File> extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            toolbar.setTitleTextColor(App.getColorFromAttr(this, R.attr.colorAccent));
        }
        setSupportActionBar(toolbar);
        final Drawable overflow = AppCompatResources.getDrawable(this, R.drawable.ic_more_vert);
        toolbar.setOverflowIcon(overflow);
        ViewPager viewPager = findViewById(R.id.tab_pager);
        setViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setScrollPosition(Preferences.getTabIndex(), 0f, true);
        viewPager.setCurrentItem(Preferences.getTabIndex());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(Build.VERSION.SDK_INT < 23 ?
                App.getColorFromAttr(this, R.attr.icon_color) :
                App.getColorFromAttr(this, R.attr.colorAccent));
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setPadding(0, App.ctx().getStatusBarHeight(), 0, 0);
        extra = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        // Set overflow menu icons visible
        if (menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:{
                startActivity(new Intent(this, SettingsActivity.class), ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle());
                return true;
            }
            case R.id.action_reset:{
                App.ctx().getPatchPreferences().edit().clear().apply();
                extra.clear();
                Toast.makeText(this, R.string.message_patch_cleared, Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_save:{
                showSaveDialog();
                return true;
            }
            case R.id.action_view:{
                showFullViewDialog();
                return true;
            }
            case R.id.action_regex_help:{
                startActivity(new Intent(this, RegexpActivity.class), ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle());
                return true;
            }
            case R.id.action_about:{
                startActivity(new Intent(this, AboutActivity.class), ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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

    private void setViewPager(ViewPager viewPager){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutPatchFragment(), getString(R.string.tab_about));
        adapter.addFragment(new ReplaceFragment(), getString(R.string.tab_match_replace));
        adapter.addFragment(new GotoFragment(), getString(R.string.tab_match_goto));
        adapter.addFragment(new AssignFragment(), getString(R.string.tab_match_assign));
        adapter.addFragment(new ru.SnowVolf.pcompiler.ui.fragment.patch.GotoFragment(), getString(R.string.tab_goto));
        adapter.addFragment(new AddFilesFragment(), getString(R.string.tab_add_files));
        adapter.addFragment(new RemoveFilesFragment(), getString(R.string.tab_remove_files));
        adapter.addFragment(new MergeFragment(), getString(R.string.tab_merge));
        adapter.addFragment(new DummyFragment(), getString(R.string.tab_dummy));
        viewPager.setAdapter(adapter);
    }

    private void showSaveDialog() {
        patch = new ArrayList<>();
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_ABOUT_PATCH));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MATCH_REPLACE));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MATCH_GOTO));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MATCH_ASSIGN));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_GOTO));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_ADD_FILES));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_REMOVE_FILES));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MERGE));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_DUMMY));

        SweetListDialog saveDialog = new SweetListDialog(this);
        saveDialog.setTitle(getString(R.string.title_save));
        saveDialog.setItems(R.array.save_items);
        saveDialog.setAdapter();
        saveDialog.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0: {
                    StringBuilder str = new StringBuilder();
                    for (String s: patch) {
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
                    for (String s: patch) {
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
    private void showFullViewDialog(){
        patch = new ArrayList<>();
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_ABOUT_PATCH));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MATCH_REPLACE));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MATCH_GOTO));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MATCH_ASSIGN));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_GOTO));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_ADD_FILES));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_REMOVE_FILES));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_MERGE));
        patch.add(StringWrapper.readFromPrefs(Constants.KEY_DUMMY));
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(this).inflate(R.layout.dialog_full_view, null);
        final TextView content = view.findViewById(R.id.content);

        Spannable spannable;
        if (Preferences.isMonospaceFontAllowed()) {
            final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/RobotoMono-Regular.ttf");
            content.setTypeface(typeface);
        }
        content.setTextSize(Preferences.getFontSize());
        StringBuilder str = new StringBuilder();
        for (String s: patch) {
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
        BottomSheetDialog dialog = new BottomSheetDialog(this, ThemeWrapper.getTheme());
        dialog.setContentView(view);
        dialog.show();
    }

    private void checkPermissionsText(){
        if (Build.VERSION.SDK_INT >= 23 && !RuntimeUtil.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            RuntimeUtil.storage(this, RuntimeUtil.REQUEST_EXTERNAL_STORAGE_TEXT);
        } else {
            StringBuilder str = new StringBuilder();
            for (String s: patch) {
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
        //dialog.setNegative(getString(android.R.string.cancel), view -> dialog.dismiss());
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
        //dialog.setNegative(getString(android.R.string.cancel), view -> dialog.dismiss());
        dialog.show();
    }

    private void zipIo(){
        File dir = new File(Preferences.getPatchOutput());
        if (!dir.exists() && !dir.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        StringBuilder str = new StringBuilder();
        for (String s: patch) {
            str.append(s);
        }
        File temp = writeToTempFile(str.toString());

        try {
            ZipFile zipFile = new ZipFile(Preferences.getPatchOutput() + fileName + ".zip");
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(0);
            zipFile.addFile(temp, parameters);
            if (!StringWrapper.readFromPrefs(Constants.KEY_EXTRA_FILES).isEmpty()) {
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
}

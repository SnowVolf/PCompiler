package ru.svolf.pcompiler.ui.fragment.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import ru.svolf.pcompiler.App;
import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.ui.fragment.dialog.SweetInputDialogFragment;

/**
 * Created by Snow Volf on 18.08.2017, 21:55
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.settings);
        setCurrentValue(findPreference("sys.language"));
        findPreference("sys.delay").setSummary(App.ctx().getPreferences().getString("sys.delay", "2000"));
        setCurrentValue(findPreference("ui.theme"));
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        init();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "ui.theme":{
                setCurrentValue((ListPreference) findPreference(key));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("org.openintents.action.REFRESH_THEME"));
                break;
            }
            case "sys.language": {
                setCurrentValue((ListPreference) findPreference(key));
                break;
            }
            case "sys.delay":{
                findPreference(key).setSummary(sharedPreferences.getString(key, "2000"));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setCurrentValue(ListPreference listPreference){
        listPreference.setSummary(listPreference.getEntry());
    }

    private void init(){
        findPreference("ui.font_size").setOnPreferenceClickListener(preference -> {
            View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_font_size, null);

            assert v != null;
            final SeekBar seekBar = v.findViewById(R.id.value_seekbar);
            seekBar.setProgress(Preferences.INSTANCE.getFontSize() - 1 - 7);
            final TextView textView = v.findViewById(R.id.value_textview);
            textView.setText(String.format(Locale.ENGLISH, "%d", seekBar.getProgress() + 1 + 7));
            textView.setTextSize(seekBar.getProgress() + 1 + 7);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    textView.setText(String.format(Locale.ENGLISH, "%d", i + 1 + 7));
                    textView.setTextSize(i + 1 + 7);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(preference.getTitle())
                    .setView(v)
                    .setPositiveButton(android.R.string.ok, (dialog1, which) -> Preferences.INSTANCE.setFontSize(seekBar.getProgress() + 1 + 7))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(R.string.menu_reset, null)
                    .show();
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v1 -> {
                seekBar.setProgress(16 - 1 - 7);
                Preferences.INSTANCE.setFontSize(16);
            });
            return false;
        });
        findPreference("preset.engine_ver").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.INSTANCE.getPatchEngineVer());
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.author").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.INSTANCE.getPatchAuthor());
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.path").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.INSTANCE.getPatchOutput());
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.archive_comment").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, null);
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.mime_type").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.INSTANCE.getMimeType());
            dialogFragment.show(manager, null);
            return true;
        });
    }
}

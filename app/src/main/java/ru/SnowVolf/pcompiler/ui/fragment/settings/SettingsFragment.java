package ru.SnowVolf.pcompiler.ui.fragment.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetInputDialogFragment;

/**
 * Created by Snow Volf on 18.08.2017, 21:55
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setCurrentValue((ListPreference) findPreference("sys.language"));
        setCurrentValue((ListPreference) findPreference("ui.theme"));
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
            seekBar.setProgress(Preferences.getFontSize() - 1 - 7);
            final TextView textView = v.findViewById(R.id.value_textview);
            textView.setText(Integer.toString(seekBar.getProgress() + 1 + 7));
            textView.setTextSize(seekBar.getProgress() + 1 + 7);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    textView.setText(Integer.toString(i + 1 + 7));
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
                    .setPositiveButton(android.R.string.ok, (dialog1, which) -> Preferences.setFontSize(seekBar.getProgress() + 1 + 7))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(R.string.menu_reset, null)
                    .show();
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v1 -> {
                seekBar.setProgress(16 - 1 - 7);
                Preferences.setFontSize(16);
            });
            return false;
        });
        findPreference("preset.engine_ver").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.getPatchEngineVer());
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.author").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.getPatchAuthor());
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.path").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.getPatchOutput());
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.archive_comment").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, null);
            dialogFragment.show(manager, null);
            return true;
        });

        findPreference("preset.mime_type").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.getMimeType());
            dialogFragment.show(manager, null);
            return true;
        });
    }
}

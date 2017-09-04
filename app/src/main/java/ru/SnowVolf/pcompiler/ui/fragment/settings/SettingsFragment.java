package ru.SnowVolf.pcompiler.ui.fragment.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetInputDialog;

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

        findPreference("preset.engine_ver").setSummary(Preferences.getPatchEngineVer());
        findPreference("preset.author").setSummary(Preferences.getPatchAuthor());
        findPreference("preset.path").setSummary(Preferences.getPatchOutput());
        findPreference("preset.archive_comment").setSummary(Preferences.getArchiveComment());
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
        findPreference("preset.engine_ver").setOnPreferenceClickListener(preference -> {
            final SweetInputDialog dialog = new SweetInputDialog(getActivity());
            dialog.getInputField().setInputType(InputType.TYPE_CLASS_PHONE);
            dialog.setTitle(preference.getTitle());
            dialog.setInputString(Preferences.getPatchEngineVer());
            dialog.setPositive(getString(android.R.string.ok), view -> {
                Preferences.setPatchEngineVer(dialog.getInputString());
                preference.setSummary(Preferences.getPatchEngineVer());
                dialog.dismiss();
            });
            //dialog.setNegative(getString(android.R.string.cancel), view -> dialog.dismiss());
            dialog.show();
            return true;
        });

        findPreference("preset.author").setOnPreferenceClickListener(preference -> {
            final SweetInputDialog dialog = new SweetInputDialog(getActivity());
            dialog.setTitle(preference.getTitle());
            dialog.setInputString(Preferences.getPatchAuthor());
            dialog.setPositive(getString(android.R.string.ok), view -> {
                Preferences.setPatchAuthor(dialog.getInputString());
                preference.setSummary(Preferences.getPatchAuthor());
                dialog.dismiss();
            });
            //dialog.setNegative(getString(android.R.string.cancel), view -> dialog.dismiss());
            dialog.show();
            return true;
        });

        findPreference("preset.path").setOnPreferenceClickListener(preference -> {
            final SweetInputDialog dialog = new SweetInputDialog(getActivity());
            dialog.setTitle(preference.getTitle());
            dialog.setInputString(Preferences.getPatchOutput());
            dialog.setPositive(getString(android.R.string.ok), view -> {
                Preferences.setPatchOutput(dialog.getInputString());
                preference.setSummary(Preferences.getPatchOutput());
                dialog.dismiss();
            });
            //dialog.setNegative(getString(android.R.string.cancel), view -> dialog.dismiss());
            dialog.show();
            return true;
        });

        findPreference("preset.archive_comment").setOnPreferenceClickListener(preference -> {
            final SweetInputDialog dialog = new SweetInputDialog(getActivity());
            dialog.setTitle(preference.getTitle());
            dialog.setInputString(Preferences.getArchiveComment());
            dialog.setPositive(getString(android.R.string.ok), view -> {
                Preferences.setArchiveComment(dialog.getInputString());
                preference.setSummary(Preferences.getArchiveComment());
                dialog.dismiss();
            });
            //dialog.setNegative(getString(android.R.string.cancel), view -> dialog.dismiss());
            dialog.show();
            return true;
        });
    }
}

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

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.settings);
        init();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        findPreference("ui.font_size").setOnPreferenceClickListener(preference -> {
            View v = getLayoutInflater().inflate(R.layout.dialog_font_size, null);

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

        findPreference("preset.path").setOnPreferenceClickListener(preference -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SweetInputDialogFragment dialogFragment = SweetInputDialogFragment.newInstance(preference, Preferences.INSTANCE.getPatchOutput());
            dialogFragment.show(manager, null);
            return true;
        });

    }
}

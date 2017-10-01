package ru.SnowVolf.pcompiler.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.SnowVolf.girl.nativex.NativeBottomSheetDialogFragment;
import ru.SnowVolf.pcompiler.App;

/**
 * Created by Snow Volf on 24.09.2017, 2:20
 */

public class SweetInputDialogFragment extends NativeBottomSheetDialogFragment {

    public SweetInputDialogFragment() { }

    private static Preference mPreference;
    private static String mValue;

    public static SweetInputDialogFragment newInstance(Preference preference, @Nullable String defaultValue) {
        SweetInputDialogFragment fragment = new SweetInputDialogFragment();
        mPreference = preference;
        mValue = defaultValue;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SweetInputDialog dialog = new SweetInputDialog(getActivity());

        dialog.setPrefTitle(mPreference.getTitle());
        dialog.setPositive(getString(android.R.string.ok), v -> {
            set(dialog.getInputString());
            mPreference.setSummary(dialog.getInputString());
            this.getDialog().cancel();
        }
        );
        dialog.setInputString(mValue == null || mValue.isEmpty() ? get() : getDefault());
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String get(){
        return App.ctx().getPreferences().getString(mPreference.getKey(), "");
    }

    private String getDefault(){
        return App.ctx().getPreferences().getString(mPreference.getKey(), mValue);
    }

    private void set(String value){
        App.ctx().getPreferences().edit().putString(mPreference.getKey(), value).apply();
    }
}

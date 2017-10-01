package ru.SnowVolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import ru.SnowVolf.girl.ui.GirlEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchBuilder;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.pcompiler.util.Constants;
import ru.SnowVolf.pcompiler.util.StringWrapper;

/**
 * Created by Snow Volf on 17.08.2017, 15:48
 */

public class AboutPatchFragment extends NativeContainerFragment {
    @BindView(R.id.field_engine_ver) GirlEditText mFieldEnige;
    @BindView(R.id.field_author) GirlEditText mFieldAuthor;
    @BindView(R.id.field_package_name) GirlEditText mFieldPackage;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.all_packages) Button mButtonAll;
    @BindView(R.id.button_clear) Button buttonClear;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_patch, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFieldEnige.setText(Preferences.getPatchEngineVer());
        mFieldAuthor.setText(Preferences.getPatchAuthor());
        mButtonAll.setOnClickListener(view -> {
            mFieldPackage.setText("*");
            mFieldPackage.setSelection(mFieldPackage.getText().length());
        });
        buttonSave.setOnClickListener(view -> {
            final String aboutPart;
            aboutPart =
                    PatchBuilder.insertAboutTag(mFieldEnige, "min_engine_ver")
                    + PatchBuilder.insertAboutTag(mFieldAuthor, "author")
                    + PatchBuilder.insertAboutTag(mFieldPackage, "package");

            StringWrapper.saveToPrefs(Constants.KEY_ABOUT_PATCH, aboutPart);
            Log.i(Constants.TAG, aboutPart);
            Snackbar.make(mFieldEnige, R.string.message_saved, Snackbar.LENGTH_SHORT).show();
        });
        buttonClear.setOnClickListener(view -> {
            mFieldEnige.setText("");
            mFieldAuthor.setText("");
            mFieldPackage.setText("");
            StringWrapper.saveToPrefs(Constants.KEY_ABOUT_PATCH, "");
        });
    }
}

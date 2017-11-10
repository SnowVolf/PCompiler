package ru.SnowVolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.girl.ui.GirlEditText;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.patch.ReactiveBuilder;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:48
 */

public class AboutPatchFragment extends TabFragment {
    @BindView(R.id.field_engine_ver) GirlEditText mFieldEnige;
    @BindView(R.id.field_author) GirlEditText mFieldAuthor;
    @BindView(R.id.field_package_name) GirlEditText mFieldPackage;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.all_packages) Button mButtonAll;
    @BindView(R.id.button_clear) Button buttonClear;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        baseInflateFragment(inflater, R.layout.fragment_about_patch);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_about));
        setTitle(getString(R.string.tab_about));
        setSubtitle(getString(R.string.subtitle_tab_about));
        // About & Dummy section cannot be used more than once
        getConfiguration().setAlone(true);
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
            final ReactiveBuilder aboutPart;
            aboutPart =
                    new ReactiveBuilder()
                    .insertAboutTag(mFieldEnige, "min_engine_ver")
                            .insertAboutTag(mFieldAuthor, "author")
                            .insertAboutTag(mFieldPackage, "package");

            PatchCollection.getCollection().setItemAt(getTag(), aboutPart);
        });
        buttonClear.setOnClickListener(view -> {
            mFieldEnige.setText("");
            mFieldAuthor.setText("");
            mFieldPackage.setText("");
            PatchCollection.getCollection().removeItemAt(getTag());
        });
    }
}

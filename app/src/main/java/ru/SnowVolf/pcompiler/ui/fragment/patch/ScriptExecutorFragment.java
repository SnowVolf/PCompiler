package ru.SnowVolf.pcompiler.ui.fragment.patch;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.girl.ui.GirlEditText;
import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.patch.ReactiveBuilder;

import ru.SnowVolf.pcompiler.tabs.TabFragment;
import ru.SnowVolf.pcompiler.ui.activity.TabbedActivity;
import ru.SnowVolf.pcompiler.util.Constants;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class ScriptExecutorFragment extends TabFragment {

    @BindView(R.id.field_comment) GirlEditText mFieldComment;
    @BindView(R.id.field_script) GirlEditText mFieldScript;
    @BindView(R.id.checkbox_smali) CheckBox mCheckbox;
    @BindView(R.id.field_main_class) GirlEditText mFieldMainClass;
    @BindView(R.id.field_entrance) GirlEditText mFieldEntrance;
    @BindView(R.id.field_param) GirlEditText mFieldParam;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_clear) Button buttonClear;
    @BindView(R.id.add) ImageButton mButtonAdd;
    @BindView(R.id.drawer_header_nick) AppCompatTextView mFileCaption;
    private final int REQUEST_ADD = 28;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        baseInflateFragment(inflater, R.layout.fragment_script_executor);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setTabTitle(getString(R.string.tab_executor));
        setTitle(getString(R.string.tab_executor));
        setSubtitle(getString(R.string.subtitle_tab_executor));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonSave.setOnClickListener(view -> {
            final ReactiveBuilder execPart;

            execPart = new ReactiveBuilder()
                    .escapeComment(mFieldComment.getText().toString())
                    .insertStartTag("execute_dex")
                    .insertTag(mFieldScript, "script")
                    .smaliTrue(mCheckbox.isChecked())
                    .insertTag(mFieldMainClass, "main_class")
                    .insertTag(mFieldEntrance, "entrance")
                    .insertTag(mFieldParam, "param")
                    .insertEndTag("execute_dex");

            PatchCollection.getCollection().setItemAt(getTag(), execPart);
        });
        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldScript.setText("");
            mFieldMainClass.setText("");
            mFieldEntrance.setText("");
            mFieldParam.setText("");
            TabbedActivity.extra.clear();
            PatchCollection.getCollection().removeItemAt(getTag());
            App.ctx().getPreferences().edit().putString(Constants.KEY_EXTRA_FILES, "").apply();
        });
        mButtonAdd.setOnClickListener(view -> add());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD:{
                    String mLastSelectedFile = App.ctx().getPreferences().getString(Constants.KEY_EXTRA_FILES, "");
                    if (!mLastSelectedFile.equals(data.getData().getPath())) {
                        App.ctx().getPreferences().edit().putString(Constants.KEY_EXTRA_DEXES, data.getData().getPath()).apply();
                        TabbedActivity.extraDex.add(new File(App.ctx().getPreferences().getString(Constants.KEY_EXTRA_DEXES, "")));
                        mFileCaption.setText(String.format(getString(R.string.title_list_of_dex), TabbedActivity.extraDex.size()));
                        Toast.makeText(getActivity(), data.getData().getPath(), Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void add() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/dex");
        startActivityForResult(intent, REQUEST_ADD);
    }
}

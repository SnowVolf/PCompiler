package ru.SnowVolf.pcompiler.ui.fragment.patch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.girl.ui.GirlEditText;
import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchBuilder;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.tabs.TabFragment;
import ru.SnowVolf.pcompiler.tabs.TabManager;
import ru.SnowVolf.pcompiler.ui.activity.TabbedActivity;
import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 17.08.2017, 15:28
 */

public class AddFilesFragment extends TabFragment {
    @BindView(R.id.field_comment) GirlEditText mFieldComment;
    @BindView(R.id.field_name) GirlEditText mFieldName;
    @BindView(R.id.field_source) GirlEditText mFieldSource;
    @BindView(R.id.field_target) GirlEditText mFieldTarget;
    @BindView(R.id.drawer_header_nick) AppCompatTextView mFileCaption;
    @BindView(R.id.checkbox_root) CheckBox mCheckBox;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_clear) Button buttonClear;
    @BindView(R.id.variants) ImageButton mButtonVariants;
    @BindView(R.id.add) ImageButton mButtonAdd;
    private final int REQUEST_ADD = 26;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_files, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(App.injectString(R.string.tab_add_files));
        //setTitle(App.injectString(R.string.tab_add_files));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonSave.setOnClickListener(view -> {
            final String addFilesPart;
            mFileCaption.setText(String.format(getString(R.string.title_list_of_files), TabbedActivity.extra.size()));
            addFilesPart = PatchBuilder.escapeComment(mFieldComment.getText().toString())
                    + PatchBuilder.insertStartTag("add_files")
                    + PatchBuilder.insertTag(mFieldName, "name")
                    + PatchBuilder.insertTag(mFieldTarget, "target")
                    + PatchBuilder.rootFolderTrue(mCheckBox.isChecked())
                    + PatchBuilder.insertTag(mFieldSource, "source")

                    + PatchBuilder.insertEndTag("add_files");
            PatchCollection.getCollection().add(TabManager.getActiveIndex(), addFilesPart);
            //Preferences.saveString(Constants.KEY_ADD_FILES, addFilesPart);
            Log.i(Constants.TAG, addFilesPart);
            Snackbar.make(mFieldComment, R.string.message_saved, Snackbar.LENGTH_SHORT).show();
        });
        mButtonAdd.setOnClickListener(view -> add());
        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldName.setText("");
            mFieldSource.setText("");
            mFieldTarget.setText("");
            mCheckBox.setChecked(false);
            TabbedActivity.extra.clear();
            Preferences.saveString(Constants.KEY_ADD_FILES, "");
            Preferences.saveString(Constants.KEY_EXTRA_FILES, "");
        });
        mButtonVariants.setOnClickListener(view -> {
            PopupMenu menu = new PopupMenu(getActivity(), mButtonVariants);
            menu.inflate(R.menu.menu_popup_variants);
            menu.setOnMenuItemClickListener(item -> {
                mFieldTarget.setText(item.getTitle());
                return true;
            });
            menu.show();
        });
    }

    private void add() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(Preferences.getMimeType());
        startActivityForResult(intent, REQUEST_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD:{
                    String mLastSelectedFile = Preferences.readString(Constants.KEY_EXTRA_FILES);
                    if (!mLastSelectedFile.equals(data.getData().getPath())) {
                        Preferences.saveString(Constants.KEY_EXTRA_FILES, data.getData().getPath());
                        TabbedActivity.extra.add(new File(Preferences.readString(Constants.KEY_EXTRA_FILES)));
                        mFileCaption.setText(String.format(getString(R.string.title_list_of_files), TabbedActivity.extra.size()));
                        Toast.makeText(getActivity(), data.getData().getPath(), Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        }
    }
}

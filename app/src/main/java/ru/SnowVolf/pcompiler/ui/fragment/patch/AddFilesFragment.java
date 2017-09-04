package ru.SnowVolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchBuilder;
import ru.SnowVolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.pcompiler.util.Constants;
import ru.SnowVolf.pcompiler.util.StringWrapper;

/**
 * Created by Snow Volf on 17.08.2017, 15:28
 */

public class AddFilesFragment extends NativeContainerFragment {
    @BindView(R.id.field_comment) TextInputEditText mFieldComment;
    @BindView(R.id.field_name) TextInputEditText mFieldName;
    @BindView(R.id.field_source) TextInputEditText mFieldSource;
    @BindView(R.id.field_target) TextInputEditText mFieldTarget;
    @BindView(R.id.checkbox_root) CheckBox mCheckBox;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_clear) Button buttonClear;
    @BindView(R.id.variants) ImageButton mButtonVariants;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_files, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonSave.setOnClickListener(view -> {
            final String addFilesPart;

            addFilesPart = PatchBuilder.escapeComment(mFieldComment.getText().toString())
                    + PatchBuilder.insertStartTag("add_files")
                    + PatchBuilder.insertTag(mFieldName, "name")
                    + PatchBuilder.insertTag(mFieldTarget, "target")
                    + PatchBuilder.rootFolderTrue(mCheckBox.isChecked())
                    + PatchBuilder.insertTag(mFieldSource, "source")
                    + PatchBuilder.insertEndTag("add_files");
            StringWrapper.saveToPrefs(Constants.KEY_ADD_FILES, addFilesPart);
            Log.i(Constants.TAG, addFilesPart);
            Snackbar.make(mFieldComment, R.string.message_saved, Snackbar.LENGTH_SHORT).show();
        });
        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldName.setText("");
            mFieldSource.setText("");
            mFieldTarget.setText("");
            mCheckBox.setChecked(false);
            StringWrapper.saveToPrefs(Constants.KEY_ADD_FILES, "");
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
}

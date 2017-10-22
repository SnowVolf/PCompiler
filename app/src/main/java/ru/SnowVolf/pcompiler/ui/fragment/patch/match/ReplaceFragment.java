package ru.SnowVolf.pcompiler.ui.fragment.patch.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import ru.SnowVolf.girl.ui.GirlEditText;
import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchBuilder;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.tabs.TabFragment;
import ru.SnowVolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 17.08.2017, 15:30
 */

public class ReplaceFragment extends TabFragment {
    @BindView(R.id.field_comment) GirlEditText mFieldComment;
    @BindView(R.id.field_name) GirlEditText mFieldName;
    @BindView(R.id.field_target) GirlEditText mFieldTarget;
    @BindView(R.id.field_find) GirlEditText mFieldFind;
    @BindView(R.id.checkbox_regex) CheckBox mCheckBox;
    @BindView(R.id.field_replace) GirlEditText mFieldReplace;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_clear) Button buttonClear;
    @BindView(R.id.variants) ImageButton mButtonVariants;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_match_replace, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(App.injectString(R.string.tab_match_replace));
        //setTitle(App.injectString(R.string.tab_match_replace));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCheckBox.setChecked(Preferences.isForceRegexpAllowed());
        buttonSave.setOnClickListener(view -> {
            final String matchReplacePart;

            matchReplacePart = PatchBuilder.escapeComment(mFieldComment.getText().toString())
                    + PatchBuilder.insertStartTag("match_replace")
                    + PatchBuilder.insertTag(mFieldName, "name")
                    + PatchBuilder.insertTag(mFieldTarget, "target")
                    + PatchBuilder.insertMatchTag(mFieldFind)
                    + PatchBuilder.regexTrue(mCheckBox.isChecked())
                    + PatchBuilder.insertReplaceTag(mFieldReplace)
                    + PatchBuilder.insertEndTag("match_replace");
            Preferences.saveString(Constants.KEY_MATCH_REPLACE, matchReplacePart);
            Log.i(Constants.TAG, matchReplacePart);
            Snackbar.make(mFieldComment, R.string.message_saved, Snackbar.LENGTH_SHORT).show();
        });
        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldName.setText("");
            mFieldTarget.setText("");
            mFieldFind.setText("");
            mFieldReplace.setText("");
            mCheckBox.setChecked(Preferences.isForceRegexpAllowed());
            Preferences.saveString(Constants.KEY_MATCH_REPLACE, "");
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

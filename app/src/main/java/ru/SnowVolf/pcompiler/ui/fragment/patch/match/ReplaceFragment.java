package ru.SnowVolf.pcompiler.ui.fragment.patch.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.girl.ui.GirlEditText;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.patch.ReactiveBuilder;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.tabs.TabFragment;

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
        super.onCreateView(inflater, container, savedInstanceState);
        baseInflateFragment(inflater, R.layout.fragment_match_replace);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_match_replace));
        setTitle(getString(R.string.tab_match_replace));
        setSubtitle(getString(R.string.subtitle_tab_mreplace));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCheckBox.setChecked(Preferences.INSTANCE.isForceRegexpAllowed());
        buttonSave.setOnClickListener(view -> {
            final ReactiveBuilder matchReplacePart;

            matchReplacePart = new ReactiveBuilder()
                    .escapeComment(mFieldComment.getText().toString())
                    .insertStartTag("match_replace")
                    .insertTag(mFieldName, "name")
                    .insertTag(mFieldTarget, "target")
                    .insertMatchTag(mFieldFind)
                    .regexTrue(mCheckBox.isChecked())
                    .insertReplaceTag(mFieldReplace)
                    .insertEndTag("match_replace");
            PatchCollection.INSTANCE.getCollection().setItemAt(getTag(), matchReplacePart);
        });
        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldName.setText("");
            mFieldTarget.setText("");
            mFieldFind.setText("");
            mFieldReplace.setText("");
            mCheckBox.setChecked(Preferences.INSTANCE.isForceRegexpAllowed());
            PatchCollection.INSTANCE.getCollection().removeItemAt(getTag());
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

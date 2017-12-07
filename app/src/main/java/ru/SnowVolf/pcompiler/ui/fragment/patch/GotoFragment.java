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
import ru.SnowVolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:27
 */

public class GotoFragment extends TabFragment {
    @BindView(R.id.field_comment) GirlEditText mFieldComment;
    @BindView(R.id.field_next_rule) GirlEditText mFieldNextRule;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_clear) Button buttonClear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        baseInflateFragment(inflater, R.layout.fragment_goto);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_goto));
        setTitle(getString(R.string.tab_goto));
        setSubtitle(getString(R.string.subtitle_tab_goto));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonSave.setOnClickListener(view -> {
            final ReactiveBuilder gotoPart;

            gotoPart = new ReactiveBuilder()
                    .escapeComment(mFieldComment.getText().toString())
                    .insertStartTag("goto")
                    .insertTag(mFieldNextRule, "goto")
                    .insertEndTag("goto");

            PatchCollection.INSTANCE.getCollection().setItemAt(getTag(), gotoPart);
        });

        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldNextRule.setText("");
            PatchCollection.INSTANCE.getCollection().removeItemAt(getTag());
        });
    }
}

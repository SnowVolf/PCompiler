package ru.SnowVolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.girl.ui.GirlEditText;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.patch.ReactiveBuilder;
import ru.SnowVolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:29
 */

public class DummyFragment extends TabFragment {
    @BindView(R.id.field_comment) GirlEditText mFieldComment;
    @BindView(R.id.field_name) GirlEditText mFieldName;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_clear) Button buttonClear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        baseInflateFragment(inflater, R.layout.fragment_dummy);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_dummy));
        setTitle(getString(R.string.tab_dummy));
        setSubtitle(getString(R.string.subtitle_tab_dummy));
        // About & Dummy section cannot be used more than once
        getConfiguration().setAlone(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonSave.setOnClickListener(view -> {
            final ReactiveBuilder dummyPart;

            dummyPart = new ReactiveBuilder()
                    .escapeComment(mFieldComment.getText().toString())
                   .insertStartTag("dummy")
                   .insertTag(mFieldName, "name")
                   .insertEndTag("dummy");

            PatchCollection.getCollection().setItemAt(getTag(), dummyPart);
        });
        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldName.setText("");
            PatchCollection.getCollection().removeItemAt(getTag());
        });
    }
}

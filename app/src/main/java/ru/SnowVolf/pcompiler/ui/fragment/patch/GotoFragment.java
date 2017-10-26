package ru.SnowVolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import ru.SnowVolf.pcompiler.util.Constants;

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
        rootView = inflater.inflate(R.layout.fragment_goto, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(App.injectString(R.string.tab_goto));
        //setTitle(App.injectString(R.string.tab_goto));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonSave.setOnClickListener(view -> {
            final String gotoPart;

            gotoPart = PatchBuilder.escapeComment(mFieldComment.getText().toString())
                    + PatchBuilder.insertStartTag("goto")
                    + PatchBuilder.insertTag(mFieldNextRule, "goto")
                    + PatchBuilder.insertEndTag("goto");

            PatchCollection.getCollection().add(TabManager.getActiveIndex(), gotoPart);
            //Preferences.saveString(Constants.KEY_GOTO, gotoPart);
            Log.i(Constants.TAG, gotoPart);
            Snackbar.make(mFieldComment, R.string.message_saved, Snackbar.LENGTH_SHORT).show();
        });

        buttonClear.setOnClickListener(view -> {
            mFieldComment.setText("");
            mFieldNextRule.setText("");
            try {
                PatchCollection.getCollection().removeItemAt(TabManager.getActiveIndex());
            } catch (Exception ignored){}
        });
    }
}

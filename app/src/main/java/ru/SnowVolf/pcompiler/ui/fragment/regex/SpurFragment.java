package ru.SnowVolf.pcompiler.ui.fragment.regex;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.pcompiler.util.StrF;

/**
 * Created by Snow Volf on 21.08.2017, 1:59
 */

public class SpurFragment extends NativeContainerFragment {

    @BindView(R.id.content) TextView content;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regex_spur, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Preferences.isMonospaceFontAllowed()) {
            final Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/RobotoMono-Regular.ttf");
            content.setTypeface(typeface);
        }
        content.setTextSize(Preferences.getFontSize());
        content.setText(StrF.parseText("regex/small_help.txt"));
    }
}

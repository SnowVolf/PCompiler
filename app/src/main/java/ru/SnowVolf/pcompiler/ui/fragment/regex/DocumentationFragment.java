package ru.SnowVolf.pcompiler.ui.fragment.regex;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.pcompiler.util.StrF;

/**
 * Created by Snow Volf on 21.08.2017, 17:58
 */

public class DocumentationFragment extends NativeContainerFragment {

    @BindView(R.id.content) TextView content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regex_spur, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_regex_documentation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_web_doc:{
                Uri uri = Uri.parse("https://github.com/zeeshanu/learn-regex/README.md");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(Intent.createChooser(intent, "learn-regex"));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Typeface mono = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoMono-Regular.ttf");
        content.setTypeface(mono);
        content.setText(StrF.parseText("regex/documentation.txt"));
    }
}

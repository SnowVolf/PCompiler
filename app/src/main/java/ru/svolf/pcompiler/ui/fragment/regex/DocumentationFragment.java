package ru.svolf.pcompiler.ui.fragment.regex;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentRegexSpurBinding;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.svolf.pcompiler.util.StrF;

/**
 * Created by Snow Volf on 21.08.2017, 17:58
 */

public class DocumentationFragment extends NativeContainerFragment {
    FragmentRegexSpurBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regex_spur, container, false);
        binding = FragmentRegexSpurBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.content.setTextSize(Preferences.INSTANCE.getFontSize());
        binding.content.setText(StrF.parseText("regex/documentation.txt"));
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
                Uri uri = Uri.parse("https://github.com/ziishaned/learn-regex");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(Intent.createChooser(intent, "learn-regex"));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

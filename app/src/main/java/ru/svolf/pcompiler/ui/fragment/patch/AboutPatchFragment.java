package ru.svolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentAboutPatchBinding;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.patch.ReactiveBuilder;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:48
 */

public class AboutPatchFragment extends TabFragment {
    FragmentAboutPatchBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentAboutPatchBinding.inflate(inflater);
        baseInflateBinding(binding);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_about));
        setTitle(getString(R.string.tab_about));
        setSubtitle(getString(R.string.subtitle_tab_about));
        // About & Dummy section cannot be used more than once
        getConfiguration().setAlone(true);
        binding.fieldEngineVer.setText(Preferences.INSTANCE.getPatchEngineVer());
        binding.fieldAuthor.setText(Preferences.INSTANCE.getPatchAuthor());
        binding.allPackages.setOnClickListener(v -> {
            binding.fieldPackageName.setText("*");
            binding.fieldPackageName.setSelection(binding.fieldPackageName.getText().length());
        });
        binding.buttonBar.buttonSave.setOnClickListener(v -> {
            final ReactiveBuilder aboutPart;
            aboutPart =
                    new ReactiveBuilder()
                            .insertAboutTag(binding.fieldEngineVer, "min_engine_ver")
                            .insertAboutTag(binding.fieldAuthor, "author")
                            .insertAboutTag(binding.fieldPackageName, "package");

            PatchCollection.getCollection().setItemAt(getTag(), aboutPart);
        });
        binding.buttonBar.buttonClear.setOnClickListener(v -> {
            binding.fieldEngineVer.clear();
            binding.fieldAuthor.clear();
            binding.fieldPackageName.clear();
            PatchCollection.getCollection().removeItemAt(getTag());
        });
    }

}

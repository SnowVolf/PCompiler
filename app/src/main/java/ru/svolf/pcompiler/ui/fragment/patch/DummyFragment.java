package ru.svolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentDummyBinding;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.patch.ReactiveBuilder;
import ru.svolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:29
 */

public class DummyFragment extends TabFragment {
    FragmentDummyBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentDummyBinding.inflate(inflater);
        baseInflateBinding(binding);
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
        binding.buttonBar.buttonSave.setOnClickListener(v -> {
            final ReactiveBuilder dummyPart;

            dummyPart = new ReactiveBuilder()
                    .escapeComment(binding.fieldComment.getText().toString())
                    .insertStartTag("dummy")
                    .insertTag(binding.fieldName, "name")
                    .insertEndTag("dummy");

            PatchCollection.getCollection().setItemAt(getTag(), dummyPart);
        });
        binding.buttonBar.buttonClear.setOnClickListener(v -> {
            binding.fieldComment.clear();
            binding.fieldName.clear();
            PatchCollection.getCollection().removeItemAt(getTag());
        });
    }

}

package ru.svolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentMergeBinding;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.patch.ReactiveBuilder;
import ru.svolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:29
 */

public class MergeFragment extends TabFragment {
    FragmentMergeBinding binding;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentMergeBinding.inflate(inflater);
        baseInflateBinding(binding);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_merge));
        setTitle(getString(R.string.tab_merge));
        setSubtitle(getString(R.string.subtitle_tab_merge));
        binding.buttonBar.buttonSave.setOnClickListener(v -> {
            final ReactiveBuilder mergePart;

            mergePart = new ReactiveBuilder()
                    .escapeComment(binding.fieldComment.getText().toString())
                    .insertStartTag("merge")
                    .insertTag(binding.fieldName, "name")
                    .insertTag(binding.fieldSource, "source")
                    .insertEndTag("merge");

            PatchCollection.getCollection().setItemAt(getTag(), mergePart);
        });
        binding.buttonBar.buttonClear.setOnClickListener(v -> {
            binding.fieldComment.clear();
            binding.fieldName.clear();
            binding.fieldSource.clear();
            PatchCollection.getCollection().removeItemAt(getTag());
        });
    }

}

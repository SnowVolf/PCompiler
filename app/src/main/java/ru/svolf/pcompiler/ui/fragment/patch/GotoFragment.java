package ru.svolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentGotoBinding;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.patch.ReactiveBuilder;
import ru.svolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:27
 */

public class GotoFragment extends TabFragment {
    FragmentGotoBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentGotoBinding.inflate(inflater);
        baseInflateBinding(binding);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_goto));
        setTitle(getString(R.string.tab_goto));
        setSubtitle(getString(R.string.subtitle_tab_goto));
        binding.buttonBar.buttonSave.setOnClickListener(v -> {
            final ReactiveBuilder gotoPart;

            gotoPart = new ReactiveBuilder()
                    .escapeComment(binding.fieldComment.getText().toString())
                    .insertStartTag("goto")
                    .insertTag(binding.fieldNextRule, "goto")
                    .insertEndTag("goto");

            PatchCollection.getCollection().setItemAt(getTag(), gotoPart);
        });

        binding.buttonBar.buttonClear.setOnClickListener(v -> {
            binding.fieldComment.clear();
            binding.fieldNextRule.clear();
            PatchCollection.getCollection().removeItemAt(getTag());
        });
    }

}

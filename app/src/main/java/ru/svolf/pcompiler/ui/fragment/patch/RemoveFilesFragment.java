package ru.svolf.pcompiler.ui.fragment.patch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentRemoveFilesBinding;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.patch.ReactiveBuilder;
import ru.svolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:28
 */

public class RemoveFilesFragment extends TabFragment {
    FragmentRemoveFilesBinding binding;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentRemoveFilesBinding.inflate(inflater);
        baseInflateBinding(binding);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_remove_files));
        setTitle(getString(R.string.tab_remove_files));
        setSubtitle(getString(R.string.subtitle_tab_remove_files));
        binding.buttonBar.buttonSave.setOnClickListener(v -> {
            final ReactiveBuilder removeFilesPart;

            removeFilesPart = new ReactiveBuilder()
                    .escapeComment(binding.fieldComment.getText().toString())
                    .insertStartTag("remove_files")
                    .insertTag(binding.fieldName, "name")
                    .insertTag(binding.fieldTarget, "target")
                    .insertEndTag("remove_files");

            PatchCollection.getCollection().setItemAt(getTag(), removeFilesPart);
            getTabActivity().updateTabList();
        });
        binding.buttonBar.buttonClear.setOnClickListener(v -> {
            binding.fieldComment.clear();
            binding.fieldTarget.clear();
            binding.fieldName.clear();
            PatchCollection.getCollection().removeItemAt(getTag());
            getTabActivity().updateTabList();
        });
        binding.variants.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getActivity(), binding.variants);
            menu.inflate(R.menu.menu_popup_variants);
            menu.setOnMenuItemClickListener(item -> {
                binding.fieldTarget.setText(item.getTitle());
                return true;
            });
            menu.show();
        });
    }

}

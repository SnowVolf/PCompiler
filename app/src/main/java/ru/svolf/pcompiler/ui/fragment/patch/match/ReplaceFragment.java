package ru.svolf.pcompiler.ui.fragment.patch.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentMatchReplaceBinding;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.patch.ReactiveBuilder;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:30
 */

public class ReplaceFragment extends TabFragment {
    FragmentMatchReplaceBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentMatchReplaceBinding.inflate(inflater);
        baseInflateBinding(binding);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_match_replace));
        setTitle(getString(R.string.tab_match_replace));
        setSubtitle(getString(R.string.subtitle_tab_mreplace));
        binding.checkboxRegex.setChecked(Preferences.INSTANCE.isForceRegexpAllowed());
        binding.buttonBar.buttonSave.setOnClickListener(v -> {
            final ReactiveBuilder matchReplacePart;

            matchReplacePart = new ReactiveBuilder()
                    .escapeComment(binding.fieldComment.getText().toString())
                    .insertStartTag("match_replace")
                    .insertTag(binding.fieldName, "name")
                    .insertTag(binding.fieldTarget, "target")
                    .insertMatchTag(binding.fieldFind)
                    .regexTrue(binding.checkboxRegex.isChecked())
                    .insertReplaceTag(binding.fieldReplace)
                    .insertEndTag("match_replace");
            PatchCollection.getCollection().setItemAt(getTag(), matchReplacePart);
        });
        binding.buttonBar.buttonClear.setOnClickListener(v -> {
            binding.fieldComment.clear();
            binding.fieldName.clear();
            binding.fieldTarget.clear();
            binding.fieldFind.clear();
            binding.fieldReplace.clear();
            binding.checkboxRegex.setChecked(Preferences.INSTANCE.isForceRegexpAllowed());
            PatchCollection.getCollection().removeItemAt(getTag());
        });
        binding.variants.setOnClickListener(v-> {
            PopupMenu menu = new PopupMenu(getContext(), v);
            menu.inflate(R.menu.menu_popup_variants);
            menu.setOnMenuItemClickListener(item -> {
                binding.fieldTarget.setText(item.getTitle());
                return true;
            });
            menu.show();
        });
    }

}

package ru.svolf.pcompiler.ui.fragment.patch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import java.io.File;

import ru.svolf.pcompiler.App;
import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentAddFilesBinding;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.patch.ReactiveBuilder;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.tabs.TabFragment;
import ru.svolf.pcompiler.ui.activity.TabbedActivity;
import ru.svolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 17.08.2017, 15:28
 */

public class AddFilesFragment extends TabFragment {
    FragmentAddFilesBinding binding;
    private final int REQUEST_ADD = 26;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentAddFilesBinding.inflate(inflater);
        baseInflateBinding(binding);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabTitle(getString(R.string.tab_add_files));
        setTitle(getString(R.string.tab_add_files));
        setSubtitle(getString(R.string.subtitle_tab_add_files));
        binding.drawerHeaderNick.setText(String.format(getString(R.string.title_list_of_files), TabbedActivity.extra.size()));
        binding.buttonBar.buttonSave.setOnClickListener(v -> {
            final ReactiveBuilder addFilesPart;
            addFilesPart = new ReactiveBuilder()
                    .escapeComment(binding.fieldComment.getText().toString())
                    .insertStartTag("add_files")
                    .insertTag(binding.fieldName, "name")
                    .insertTag(binding.fieldTarget, "target")
                    .rootFolderTrue(binding.checkboxRoot.isChecked())
                    .insertTag(binding.fieldSource, "source")
                    .insertEndTag("add_files");

            PatchCollection.getCollection().setItemAt(getTag(), addFilesPart);
            getTabActivity().updateTabList();
        });
        binding.add.setOnClickListener(v -> add());
        binding.buttonBar.buttonClear.setOnClickListener(v -> {
            binding.fieldComment.clear();
            binding.fieldName.clear();
            binding.fieldSource.clear();
            binding.fieldTarget.clear();
            binding.checkboxRoot.setChecked(false);
            TabbedActivity.extra.clear();
            PatchCollection.getCollection().removeItemAt(getTag());
            App.ctx().getPreferences().edit().putString(Constants.INSTANCE.getKEY_EXTRA_DEXES(), "").apply();
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


    private void add() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(Preferences.INSTANCE.getMimeType());
        startActivityForResult(intent, REQUEST_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD:{
                    String mLastSelectedFile = App.ctx().getPreferences().getString(Constants.INSTANCE.getKEY_EXTRA_DEXES(), "");
                    if (!mLastSelectedFile.equals(data.getData().getPath())) {
                        App.ctx().getPreferences().edit().putString(Constants.INSTANCE.getKEY_EXTRA_DEXES(), data.getData().getPath()).apply();
                        TabbedActivity.extra.add(new File(App.ctx().getPreferences().getString(Constants.INSTANCE.getKEY_EXTRA_DEXES(), "")));
                        binding.drawerHeaderNick.setText(String.format(getString(R.string.title_list_of_files), TabbedActivity.extra.size()));
                        Toast.makeText(getActivity(), data.getData().getPath(), Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        }
    }
}

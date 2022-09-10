package ru.svolf.pcompiler.ui.fragment.regex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentRegexSpurBinding;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.svolf.pcompiler.util.StrF;

/**
 * Created by Snow Volf on 21.08.2017, 1:59
 */

public class SpurFragment extends NativeContainerFragment {
    FragmentRegexSpurBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegexSpurBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.content.setTextSize(Preferences.INSTANCE.getFontSize());
        binding.content.setText(StrF.parseText("regex/small_help.txt"));
    }
}

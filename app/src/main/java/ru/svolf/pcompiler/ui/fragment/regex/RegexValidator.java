package ru.svolf.pcompiler.ui.fragment.regex;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.FragmentRegexValidatorBinding;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.ui.fragment.NativeContainerFragment;

/**
 * Created by Snow Volf on 21.08.2017, 13:20
 */

public class RegexValidator extends NativeContainerFragment {
    FragmentRegexValidatorBinding binding;
    private Flags flags = new Flags();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegexValidatorBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.regexText.setTextSize(Preferences.INSTANCE.getFontSize());
        binding.plainText.setTextSize(Preferences.INSTANCE.getFontSize());
        binding.regexCount.setTextSize(Preferences.INSTANCE.getFontSize());
        binding.regexResult.setTextSize(Preferences.INSTANCE.getFontSize());
        binding.regexFlags.setTextSize(Preferences.INSTANCE.getFontSize());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_regex_validator, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_flags:{
                showFlagsList();
                return true;
            }
            case R.id.action_clear:{
                clearAllText();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        binding.regexFlags.setText(flags.getFlagString());

        binding.regexText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!binding.plainText.getText().toString().isEmpty()){
                    RegexEx();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.plainText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                  Фикс бага htc 600
                  Когда при пустом поле regex, все выражения были совпадающими
                 */
                RegexEx();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void RegexEx(){
        String rV = binding.regexText.getText().toString();
        if (rV.equals("")){
            binding.regexCount.setText(R.string.regex_def_match);
            binding.regexResult.setText(binding.plainText.getText());
            return;
        }
        try {
            //noinspection WrongConstant
            Matcher m = Pattern.compile(rV, flags.getFlags()).matcher(binding.plainText.getText());
            Spannable spannable = new SpannableString(binding.plainText.getText());
            int i = 0;
            while (m.find()){
                spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.md_theme_light_surfaceTint)), m.start(), m.end(), 33);
                i++;
            }
            binding.regexResult.setText(spannable);
            if (i == 1) {
                binding.regexCount.setText(String.format(getString(R.string.regex_def_match_one), i));
            } else binding.regexCount.setText(String.format(getString(R.string.regex_def_match_few), i));
        } catch (PatternSyntaxException pse) {
            Snackbar.make(binding.regexCount, pse.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private void showFlagsList(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_regexp_flags)
                .setMultiChoiceItems(flags.getOptions(), flags.getSelectedBooleans(), (dialogInterface, zoe, isChecked) -> {
                    if (isChecked)
                        flags.add(zoe);
                    else
                        flags.remove(zoe);

                }).setPositiveButton(android.R.string.ok, (dialogInterface, volf) -> {
                    binding.regexFlags.setText(flags.getFlagString());
                    /*
                      Проверяем поля на пустоту, сделано для того чтобы не применял regex к пустому выражению "".
                      Иначе получается, если в {@param binding.plainText} пусто, то возникает коллапс
                      "" = ""  = 1 совпадение
                     */
                    if (!binding.regexText.getText().toString().isEmpty() && !binding.plainText.getText().toString().isEmpty()) {
                        RegexEx();
                    }
                }).show();

    }

    private void clearAllText(){
        binding.plainText.clear();
        binding.regexText.clear();
        binding.regexCount.setText("");
        binding.regexResult.setText("");
    }
}

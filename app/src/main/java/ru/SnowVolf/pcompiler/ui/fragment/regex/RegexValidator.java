package ru.SnowVolf.pcompiler.ui.fragment.regex;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.girl.ui.GirlEditText;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.ui.fragment.NativeContainerFragment;

/**
 * Created by Snow Volf on 21.08.2017, 13:20
 */

public class RegexValidator extends NativeContainerFragment {
    @BindView(R.id.regex_text) GirlEditText regexVal;
    @BindView(R.id.plain_text) GirlEditText sourceSoup;
    @BindView(R.id.regex_count) TextView counter;
    @BindView(R.id.regex_result) TextView getCounterResult;
    @BindView(R.id.regex_flags) TextView currentFlag;
    private Flags flags = new Flags();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regex_validator, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Preferences.isMonospaceFontAllowed()) {
            final Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoMono-Regular.ttf");
            regexVal.setTypeface(typeface);
            sourceSoup.setTypeface(typeface);
            counter.setTypeface(typeface);
            getCounterResult.setTypeface(typeface);
            currentFlag.setTypeface(typeface);
        }
        regexVal.setTextSize(Preferences.getFontSize());
        sourceSoup.setTextSize(Preferences.getFontSize());
        counter.setTextSize(Preferences.getFontSize());
        getCounterResult.setTextSize(Preferences.getFontSize());
        currentFlag.setTextSize(Preferences.getFontSize());
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

        currentFlag.setText(flags.getFlagString());

        regexVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!sourceSoup.getText().toString().isEmpty()){
                    RegexEx();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sourceSoup.addTextChangedListener(new TextWatcher() {
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
        String rV = regexVal.getText().toString();
        if (rV.equals("")){
            counter.setText(R.string.regex_def_match);
            getCounterResult.setText(sourceSoup.getText());
            return;
        }
        try {
            //noinspection WrongConstant
            Matcher m = Pattern.compile(rV, flags.getFlags()).matcher(sourceSoup.getText());
            Spannable spannable = new SpannableString(sourceSoup.getText());
            int i = 0;
            while (m.find()){
                spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.dark_colorAccent)), m.start(), m.end(), 33);
                i++;
            }
            getCounterResult.setText(spannable);
            if (i == 1) {
                counter.setText(String.format(getString(R.string.regex_def_match_one), i));
            } else counter.setText(String.format(getString(R.string.regex_def_match_few), i));
        } catch (PatternSyntaxException pse) {
            Snackbar.make(counter, pse.getMessage(), Snackbar.LENGTH_LONG).show();
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
                    currentFlag.setText(flags.getFlagString());
                    /*
                      Проверяем поля на пустоту, сделано для того чтобы не применял regex к пустому выражению "".
                      Иначе получается, если в {@param sourceSoup} пусто, то возникает коллапс
                      "" = ""  = 1 совпадение
                     */
                    if (!regexVal.getText().toString().isEmpty() && !sourceSoup.getText().toString().isEmpty()) {
                        RegexEx();
                    }
                }).show();

    }

    private void clearAllText(){
        sourceSoup.setText("");
        regexVal.setText("");
        counter.setText("");
        getCounterResult.setText("");
    }
}

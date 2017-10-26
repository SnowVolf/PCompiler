package ru.SnowVolf.pcompiler.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.util.StrF;

/**
 * Created by Snow Volf on 26.10.2017, 22:49
 */

public class HelpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView content = findViewById(R.id.textView);
        content.setText(StrF.parseText("help/help.txt"));
        Button read = findViewById(R.id.buttonPanel);
        read.setOnClickListener(v -> {
            Preferences.setHelpShowed();
            finishAfterTransition();
        });
    }

    @Override
    public void onBackPressed() {}
}

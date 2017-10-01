package ru.SnowVolf.pcompiler.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.ui.fragment.settings.SettingsFragment;

/**
 * Created by Snow Volf on 17.08.2017, 21:14
 */

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, App.ctx().getStatusBarHeight(), 0, 0);
        if (Build.VERSION.SDK_INT >= 23) {
            toolbar.setTitleTextColor(App.getColorFromAttr(this, R.attr.colorAccent));
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setElevation(App.dpToPx(4));
        getFragmentManager().
                beginTransaction()
                .replace(R.id.frame_container, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

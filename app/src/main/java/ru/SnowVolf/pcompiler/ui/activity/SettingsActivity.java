package ru.SnowVolf.pcompiler.ui.activity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.acra.ACRA;

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
            toolbar.setTitleTextColor(App.getColorFromAttr(this, R.attr.icon_color));
        }
        toolbar.getMenu().add("Debug menu")
                .setIcon(R.drawable.ic_bug)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    debugMenu();
                    return true;
                });
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setElevation(App.dpToPx(4));
        getSupportFragmentManager().
                beginTransaction()
                .replace(R.id.frame_container, new SettingsFragment())
                .commit();
    }

    private void debugMenu() {
        final CharSequence[] items = new CharSequence[]{"Throw an exception"};
        new AlertDialog.Builder(this)
                .setTitle("Debug menu")
                .setItems(items, (dialogInterface, i) -> {
                    switch (i){
                        case 0:{
                            try {
                                throw new RuntimeException("DevException");
                            } catch (RuntimeException ex){
                                ACRA.getErrorReporter().handleException(ex);
                            }
                            break;
                        }
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

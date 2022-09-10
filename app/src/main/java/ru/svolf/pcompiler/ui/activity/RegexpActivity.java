package ru.svolf.pcompiler.ui.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ru.svolf.pcompiler.App;
import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.adapter.ViewPagerAdapter;
import ru.svolf.pcompiler.ui.fragment.regex.DocumentationFragment;
import ru.svolf.pcompiler.ui.fragment.regex.RegexValidator;
import ru.svolf.pcompiler.ui.fragment.regex.SpurFragment;

/**
 * Created by Snow Volf on 21.08.2017, 0:09
 */

public class RegexpActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regexp);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, App.ctx().getStatusBarHeight(), 0, 0);
        if (Build.VERSION.SDK_INT >= 23) {
            toolbar.setTitleTextColor(App.getColorFromAttr(this, R.attr.icon_color));
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        ViewPager viewPager = findViewById(R.id.tab_pager);
        setViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RegexValidator(), getString(R.string.tab_regex));
        adapter.addFragment(new SpurFragment(), getString(R.string.tab_help));
        adapter.addFragment(new DocumentationFragment(), getString(R.string.tab_documentation));
        viewPager.setAdapter(adapter);
    }

}

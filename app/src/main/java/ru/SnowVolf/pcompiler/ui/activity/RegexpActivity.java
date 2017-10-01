package ru.SnowVolf.pcompiler.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.adapter.ViewPagerAdapter;
import ru.SnowVolf.pcompiler.ui.fragment.regex.DocumentationFragment;
import ru.SnowVolf.pcompiler.ui.fragment.regex.RegexValidator;
import ru.SnowVolf.pcompiler.ui.fragment.regex.SpurFragment;

/**
 * Created by Snow Volf on 21.08.2017, 0:09
 */

public class RegexpActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, App.ctx().getStatusBarHeight(), 0, 0);
        if (Build.VERSION.SDK_INT >= 23) {
            toolbar.setTitleTextColor(App.getColorFromAttr(this, R.attr.colorAccent));
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new RegexValidator(), getString(R.string.tab_regex));
        adapter.addFragment(new SpurFragment(), getString(R.string.tab_help));
        adapter.addFragment(new DocumentationFragment(), getString(R.string.tab_documentation));
        viewPager.setAdapter(adapter);
    }

}

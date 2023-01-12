package ru.svolf.pcompiler.ui.activity;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
    private final int[] titles = { R.string.tab_regex, R.string.tab_help, R.string.tab_documentation };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regexp);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, App.ctx().getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        ViewPager2 viewPager = findViewById(R.id.tab_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        setViewPager(viewPager);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles[position]));
        mediator.attach();
    }

    private void setViewPager(ViewPager2 viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new RegexValidator(), titles[0]);
        adapter.addFragment(new SpurFragment(), titles[1]);
        adapter.addFragment(new DocumentationFragment(), titles[2]);
        viewPager.setAdapter(adapter);
    }

}

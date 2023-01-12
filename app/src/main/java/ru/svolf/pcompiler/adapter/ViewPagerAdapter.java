package ru.svolf.pcompiler.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import ru.svolf.pcompiler.App;

/**
 * Created by Snow Volf on 10.06.2017, 1:06
 */

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentList = new ArrayList<>();
    private final ArrayList<String> fragmentTitleList = new ArrayList<>();


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    public void addFragment(Fragment fragment, int title){
        fragmentList.add(fragment);
        fragmentTitleList.add(App.injectString(title));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}


package ru.SnowVolf.pcompiler.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 21.10.2017, 12:30
 *
 * Create and manage tabs
 */

public class TabManager {
    private final static String TAB_PREFIX = "tab_";
    private final static String BUNDLE_PREFIX = "tab_manager_";
    private final static String BUNDLE_ACTIVE_TAG = "active_tag";
    private final static String BUNDLE_ACTIVE_INDEX = "active_index";
    private static TabManager instance;
    private FragmentManager fragmentManager;
    private TabListener tabListener;
    private static String activeTag = "";
    private static int activeIndex = 0;
    private List<TabFragment> existingFragments = new ArrayList<>();

    public interface TabListener {
        void onAddTab(TabFragment fragment);

        void onRemoveTab(TabFragment fragment);

        void onSelectTab(TabFragment fragment);

        void onChange();
    }

    public static TabManager init(AppCompatActivity activity, TabListener listener) {
        if (instance != null) {
            instance.clear();
            instance = null;
        }
        instance = new TabManager(activity, listener);
        return instance;
    }

    private void clear() {
        fragmentManager =null;
        tabListener = null;
        existingFragments.clear();
        existingFragments = null;
    }

    public static TabManager getInstance() {
        return instance;
    }

    public TabManager(AppCompatActivity activity, TabListener listener) {
        fragmentManager = activity.getSupportFragmentManager();
        tabListener = listener;
        updateFragmentList();
    }

    public void saveState(Bundle outState) {
        if (outState == null) return;
        outState.putString(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_TAG), activeTag);
        outState.putInt(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_INDEX), activeIndex);
    }

    public void loadState(Bundle state) {
        if (state == null) return;
        activeTag = state.getString(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_TAG), "");
        activeIndex = state.getInt(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_INDEX), 0);
        Log.d(Constants.TAG, "LOAD STATE " + activeTag + " : " + activeIndex);
    }

    public int getSize() {
        return existingFragments.size();
    }

    public static String getActiveTag() {
        return activeTag;
    }

    public static int getActiveIndex() {
        return activeIndex;
    }

    public List<TabFragment> getFragments() {
        return existingFragments;
    }

    public void updateFragmentList() {
        existingFragments.clear();
        if (fragmentManager.getFragments() == null) return;
        for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
            if (fragmentManager.getFragments().get(i) != null)
                existingFragments.add((TabFragment) fragmentManager.getFragments().get(i));
        }
        Collections.sort(existingFragments, (o1, o2) -> o1.getTag().compareTo(o2.getTag()));
        Log.e(Constants.TAG, "UPDATE");
        for (TabFragment fragment : existingFragments) {
            Log.e(Constants.TAG, "RECOVERY FRAGMENT " + fragment);
        }
    }

    private void hideTabs(FragmentTransaction transaction) {
        for (Fragment fragment : existingFragments) {
            if (fragment != null && !fragment.isHidden()) {
                transaction.hide(fragment);
                fragment.onPause();
            }
        }
    }

    private TabFragment findTabByTag(String tag) {
        if (tag == null) return null;
        for (TabFragment tab : existingFragments)
            if (tab.getTag().equals(tag))
                return tab;
        return null;
    }

    public TabFragment getActive() {
        return get(activeIndex);
    }

    public TabFragment get(final int index) {
        return existingFragments.get(index);
    }

    public TabFragment get(final String tag) {
        if (tag == null) return null;
        for (Fragment fragment : existingFragments)
            if (fragment.getTag().equals(tag))
                return (TabFragment) fragment;
        return null;
    }

    public void add(TabFragment tabFragment) {
        if (tabFragment == null)
            return;
        String check = null;
        if (tabFragment.getConfiguration().isAlone()) {
            check = getTagContainClass(tabFragment.getClass());
        }
        if (check != null) {
            select(check);
            return;
        }

        activeTag = TAB_PREFIX.concat(Long.toString(System.currentTimeMillis()));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideTabs(transaction);
        transaction.add(R.id.fragments_container, tabFragment, activeTag).commit();
        fragmentManager.executePendingTransactions();
        updateFragmentList();
        activeIndex = existingFragments.indexOf(tabFragment);
        tabListener.onChange();
        tabListener.onAddTab(tabFragment);
    }

    public String getTagContainClass(final Class aClass) {
        String className = aClass.getSimpleName();
        for (TabFragment fragment : existingFragments)
            if (fragment.getClass().getSimpleName().equals(className)) return fragment.getTag();
        return null;
    }

    public void remove(final String tag) {
        remove(get(tag));
    }

    public void remove(TabFragment tabFragment) {
        if (tabFragment == null)
            return;

        fragmentManager.beginTransaction().remove(tabFragment).commit();
        fragmentManager.executePendingTransactions();
        updateFragmentList();

        TabFragment parent = null;
        if (tabFragment.getParentTag() != null && !tabFragment.getParentTag().equals(""))
            parent = findTabByTag(tabFragment.getParentTag());

        if (parent == null) {
            if (existingFragments.size() >= 1) {
                if (existingFragments.size() <= activeIndex)
                    activeIndex = existingFragments.size() - 1;

                activeTag = existingFragments.get(activeIndex).getTag();
            } else {
                activeIndex = 0;
                activeTag = "";
            }
        } else {
            activeTag = tabFragment.getParentTag();
            activeIndex = existingFragments.indexOf(parent);
        }

        select(activeTag);
        tabListener.onChange();
        tabListener.onRemoveTab(tabFragment);
    }

    public void select(final String tag) {
        select(get(tag));
    }

    public void select(TabFragment tabFragment) {
        if (tabFragment == null)
            return;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideTabs(transaction);
        transaction.show(tabFragment).commit();
        tabFragment.onResume();
        fragmentManager.executePendingTransactions();
        updateFragmentList();
        activeTag = tabFragment.getTag();
        activeIndex = existingFragments.indexOf(tabFragment);
        tabListener.onChange();
        tabListener.onSelectTab(tabFragment);
    }
}


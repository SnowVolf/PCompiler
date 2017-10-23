package ru.SnowVolf.pcompiler.ui.widget.drawers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.tabs.TabFragment;
import ru.SnowVolf.pcompiler.tabs.TabManager;
import ru.SnowVolf.pcompiler.ui.activity.AboutActivity;
import ru.SnowVolf.pcompiler.ui.activity.RegexpActivity;
import ru.SnowVolf.pcompiler.ui.activity.SettingsActivity;
import ru.SnowVolf.pcompiler.ui.activity.TabbedActivity;
import ru.SnowVolf.pcompiler.ui.widget.drawers.adapters.MenuAdapter;
import ru.SnowVolf.pcompiler.ui.widget.drawers.adapters.TabAdapter;
import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by radiationx on 02.05.17.
 */

public class Drawers {
    private TabbedActivity activity;
    private DrawerLayout drawerLayout;

    private NavigationView menuDrawer;
    private RecyclerView menuListView;
    private MenuAdapter menuAdapter;
    private MenuItems allMenuItems = new MenuItems();
    private ArrayList<MenuItems.MenuItem> menuItems = new ArrayList<>();
    private MenuItems.MenuItem lastActive;

    private NavigationView tabDrawer;
    private RecyclerView tabListView;
    private TabAdapter tabAdapter;

    public Drawers(TabbedActivity activity, DrawerLayout drawerLayout) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        menuDrawer = activity.findViewById(R.id.menu_drawer);
        tabDrawer = activity.findViewById(R.id.tab_drawer);

        menuListView = activity.findViewById(R.id.menu_list);
        tabListView = activity.findViewById(R.id.tab_list);

        menuListView.setLayoutManager(new LinearLayoutManager(activity));
        tabListView.setLayoutManager(new LinearLayoutManager(activity));


        menuAdapter = new MenuAdapter(menuItems);
        tabAdapter = new TabAdapter();

        menuListView.setAdapter(menuAdapter);
        tabListView.setAdapter(tabAdapter);
    }

    public NavigationView getMenuDrawer() {
        return menuDrawer;
    }

    public NavigationView getTabDrawer() {
        return tabDrawer;
    }

    public void init(Bundle savedInstanceState) {
        initMenu();
        initTabs(savedInstanceState);

        MenuItems.MenuItem item = null;
        if (savedInstanceState != null) {
            TabFragment tabFragment = TabManager.getInstance().get(TabManager.getActiveTag());
            if (tabFragment != null) {
                item = findMenuItem(tabFragment.getClass());
            }

            Log.e(Constants.TAG, "AAAA " + tabFragment + " : " + item);
            if (item != null) {
                item.setAttachedTabTag(tabFragment.getTag());
                item.setActive(true);
                lastActive = item;
            }
        }
        Log.e(Constants.TAG, "FINAL ITEM " + item);
        selectMenuItem(item);
    }

    public void destroy() {
    }

    public void setStatusBarHeight(int height) {
        menuDrawer.setPadding(0, height, 0, 0);
        tabDrawer.setPadding(0, height, 0, 0);
    }

    private void initMenu() {
        fillMenuItems();
        menuAdapter.setItemClickListener((menuItem, position) -> {
            selectMenuItem(menuItem);
            closeMenu();
        });
    }

    private void fillMenuItems() {
        menuItems.clear();
        menuItems.addAll(allMenuItems.getCreatedMenuItems());
    }

    private void selectMenuItem(MenuItems.MenuItem item) {
        Log.e(Constants.TAG, "selectMenuItem " + item);
        if (item == null) return;
        try {
            if (item.getTabClass() == null) {
                switch (item.getAction()) {
                    case MenuItems.ACTION_APP_REGEXP: {
                        activity.startActivity(new Intent(activity, RegexpActivity.class));
                        break;
                    }
                    case MenuItems.ACTION_APP_SETTINGS: {
                        activity.startActivity(new Intent(activity, SettingsActivity.class));
                        break;
                    }
                    case MenuItems.ACTION_APP_INFO: {
                        activity.startActivity(new Intent(activity, AboutActivity.class));
                        break;
                    }
                }

            } else {
                TabFragment tabFragment = TabManager.getInstance().get(item.getAttachedTabTag());
                if (tabFragment == null) {
                    for (TabFragment fragment : TabManager.getInstance().getFragments()) {
                        if (fragment.getClass() == item.getTabClass() && fragment.getConfiguration().isMenu()) {
                            tabFragment = fragment;
                            break;
                        }
                    }
                }

                if (tabFragment == null) {
                    tabFragment = item.getTabClass().newInstance();
                    tabFragment.getConfiguration().setMenu(true);
                    TabManager.getInstance().add(tabFragment);
                    item.setAttachedTabTag(tabFragment.getTag());
                } else {
                    TabManager.getInstance().select(tabFragment);
                }

                if (lastActive != null)
                    lastActive.setActive(false);
                item.setActive(true);
                lastActive = item;
                menuAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActiveMenu(TabFragment fragment) {
        for (MenuItems.MenuItem item : menuItems) {
            if (item.getTabClass() == fragment.getClass()) {
                if (lastActive != null)
                    lastActive.setActive(false);
                item.setActive(true);
                item.setAttachedTabTag(fragment.getTag());
                lastActive = item;
                menuAdapter.notifyDataSetChanged();
            }
        }
    }

    private MenuItems.MenuItem findMenuItem(String className) {
        for (MenuItems.MenuItem item : menuItems) {
            if (item.getTabClass().getSimpleName().equals(className))
                return item;
        }
        return null;
    }

    private MenuItems.MenuItem findMenuItem(Class<? extends TabFragment> classObject) {
        for (MenuItems.MenuItem item : menuItems) {
            if (item.getTabClass() == classObject)
                return item;
        }
        return null;
    }

    public void openMenu() {
        drawerLayout.openDrawer(menuDrawer);
    }

    public void closeMenu() {
        drawerLayout.closeDrawer(menuDrawer);
    }

    public void toggleMenu() {
        if (drawerLayout.isDrawerOpen(menuDrawer)) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    private void initTabs(Bundle savedInstanceState) {
        tabAdapter.setItemClickListener((tabFragment, position) -> {
            TabManager.getInstance().select(tabFragment);
            closeTabs();
        });
        tabAdapter.setCloseClickListener((tabFragment, position) -> {
            try {
                PatchCollection.getCollection().remove(position);
            } catch (IndexOutOfBoundsException ignored){}
            TabManager.getInstance().remove(tabFragment);
            if (TabManager.getInstance().getSize() < 1) {
                activity.finish();
            }
        });
        TabManager.getInstance().loadState(savedInstanceState);
        TabManager.getInstance().updateFragmentList();
    }

    public void notifyTabsChanged() {
        tabAdapter.notifyDataSetChanged();
    }

    public void openTabs() {
        drawerLayout.openDrawer(tabDrawer);
    }

    public void closeTabs() {
        drawerLayout.closeDrawer(tabDrawer);
    }

    public void toggleTabs() {
        if (drawerLayout.isDrawerOpen(tabDrawer)) {
            closeTabs();
        } else {
            openTabs();
        }
    }
}

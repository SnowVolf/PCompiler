package ru.SnowVolf.pcompiler.ui.widget.drawers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.PatchCollection;
import ru.SnowVolf.pcompiler.settings.Preferences;
import ru.SnowVolf.pcompiler.tabs.TabFragment;
import ru.SnowVolf.pcompiler.tabs.TabManager;
import ru.SnowVolf.pcompiler.ui.activity.AboutActivity;
import ru.SnowVolf.pcompiler.ui.activity.RegexpActivity;
import ru.SnowVolf.pcompiler.ui.activity.SettingsActivity;
import ru.SnowVolf.pcompiler.ui.activity.TabbedActivity;
import ru.SnowVolf.pcompiler.ui.fragment.patch.AboutPatchFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.DummyFragment;
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

    private DrawerLayout.DrawerListener l;

    public Drawers(TabbedActivity activity, DrawerLayout drawerLayout) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;

        l = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset >= 0.4f){
                    activity.hideKeyboard();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //activity.getToolbar().setNavigationIcon(R.drawable.ic_arrow_back);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //activity.getToolbar().setNavigationIcon(R.drawable.ic_menu_hamburger);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };

        menuDrawer = activity.findViewById(R.id.menu_drawer);
        tabDrawer = activity.findViewById(R.id.tab_drawer);
        drawerLayout.addDrawerListener(l);
        menuListView = activity.findViewById(R.id.menu_list);
        tabListView = activity.findViewById(R.id.tab_list);

        menuListView.setLayoutManager(new LinearLayoutManager(activity));
        tabListView.setLayoutManager(new LinearLayoutManager(activity));

        menuAdapter = new MenuAdapter(menuItems);
        tabAdapter = new TabAdapter();

        menuListView.setAdapter(menuAdapter);
        tabListView.setAdapter(tabAdapter);

        menuListView.setHasFixedSize(true);

        final TextView nick = activity.findViewById(R.id.nick);
        nick.setText(Preferences.INSTANCE.getPatchAuthor());

        final ImageButton popup = activity.findViewById(R.id.popup_menu);
        popup.setOnClickListener(v -> {
            final PopupMenu popupMenu = new PopupMenu(activity, v);
            popupMenu.inflate(R.menu.menu_popup_extra);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.action_regex_help: {
                        activity.startActivity(new Intent(activity, RegexpActivity.class));
                        return true;
                    }
                    case R.id.action_settings: {
                        activity.startActivity(new Intent(activity, SettingsActivity.class));
                        return true;
                    }
                    case R.id.action_about: {
                        activity.startActivity(new Intent(activity, AboutActivity.class));
                        return true;
                    }
                }
                return false;
            });
            MenuPopupHelper helper = new MenuPopupHelper(activity, (MenuBuilder) popupMenu.getMenu(), v);
            helper.setForceShowIcon(true);
            helper.show();
        });
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

            Log.e(Constants.INSTANCE.getTAG(), String.format("INIT : TabFragment = %s, MenuItem = %s", tabFragment, item));
            if (item != null) {
                item.setAttachedTabTag(tabFragment.getTag());
                item.setActive(true);
                lastActive = item;
            }
        }
        Log.e(Constants.INSTANCE.getTAG(), "FINAL ITEM " + item);
        selectMenuItem(item);
    }

    public void destroy() {
        drawerLayout.removeDrawerListener(l);
    }

    public void setStatusBarHeight(int height) {
        //menuDrawer.setPadding(0, height, 0, 0);
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
        Log.e(Constants.INSTANCE.getTAG(), "selectMenuItem " + item);
        if (item == null) {
            return;
        }
        try {
             //About & Dummy section cannot be used more than once
            if (item.getTabClass() == AboutPatchFragment.class || item.getTabClass() == DummyFragment.class){
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
            } else {
                // Adding another fragments
                TabFragment newTab = item.getTabClass().newInstance();
                newTab.getConfiguration().setMenu(true);
                TabManager.getInstance().add(newTab);
            }
            notifyTabsChanged();
        } catch (Exception e) {
            Toast.makeText(activity, "An error occurred while executing for task : selectMenuItem\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
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
            PatchCollection.getCollection().remove(tabFragment.getTag());
            TabManager.getInstance().remove(tabFragment);
            if (TabManager.getInstance().getSize() < 1) {
                activity.finish();
            }
        });
        if (savedInstanceState != null) {
            TabManager.getInstance().loadState(savedInstanceState);
        } else {
            try {
                final TabFragment newTab = new AboutPatchFragment();
                newTab.getConfiguration().setMenu(true);
                TabManager.getInstance().add(newTab);

                // Adding a new (default tab)
                final TabFragment defTab = Preferences.INSTANCE.getStartupTab();
                defTab.getConfiguration().setMenu(true);
                TabManager.getInstance().add(defTab);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
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

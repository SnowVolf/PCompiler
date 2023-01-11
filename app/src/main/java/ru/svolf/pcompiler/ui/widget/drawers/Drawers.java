package ru.svolf.pcompiler.ui.widget.drawers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roacult.backdrop.BackdropLayout;

import java.util.ArrayList;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.settings.Preferences;
import ru.svolf.pcompiler.tabs.TabFragment;
import ru.svolf.pcompiler.tabs.TabManager;
import ru.svolf.pcompiler.ui.activity.AboutActivity;
import ru.svolf.pcompiler.ui.activity.RegexpActivity;
import ru.svolf.pcompiler.ui.activity.SettingsActivity;
import ru.svolf.pcompiler.ui.activity.TabbedActivity;
import ru.svolf.pcompiler.ui.fragment.patch.AboutPatchFragment;
import ru.svolf.pcompiler.ui.fragment.patch.DummyFragment;
import ru.svolf.pcompiler.ui.widget.drawers.adapters.MenuAdapter;
import ru.svolf.pcompiler.ui.widget.drawers.adapters.TabAdapter;
import ru.svolf.pcompiler.util.Constants;

/**
 * Created by radiationx on 02.05.17.
 */

public class Drawers {
    private BackdropLayout backdropLayout;
    private TabbedActivity activity;
    private RecyclerView menuListView;
    private MenuAdapter menuAdapter;
    private MenuItems allMenuItems = new MenuItems();
    private ArrayList<MenuItems.MenuItem> menuItems = new ArrayList<>();
    private MenuItems.MenuItem lastActive;

    private RecyclerView tabListView;
    private TabAdapter tabAdapter;

    @SuppressLint("RestrictedApi")
    public Drawers(TabbedActivity activity, BackdropLayout backdropLayout) {
        this.activity = activity;
        this.backdropLayout = backdropLayout;

        menuListView = activity.findViewById(R.id.menu_rules);
        tabListView = activity.findViewById(R.id.menu_tabs);

        menuAdapter = new MenuAdapter(menuItems);
        tabAdapter = new TabAdapter();

        menuListView.setAdapter(menuAdapter);
        tabListView.setAdapter(tabAdapter);

        //menuListView.setHasFixedSize(true);
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

    private void initMenu() {
        fillMenuItems();
        menuAdapter.setItemClickListener((menuItem, position) -> {
            selectMenuItem(menuItem);
            backdropLayout.close();
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

    private void initTabs(Bundle savedInstanceState) {
        tabAdapter.setItemClickListener((tabFragment, position) -> {
            TabManager.getInstance().select(tabFragment);
            //closeTabs();
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

}

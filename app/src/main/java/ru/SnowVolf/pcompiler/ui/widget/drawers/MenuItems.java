package ru.SnowVolf.pcompiler.ui.widget.drawers;

import android.support.annotation.DrawableRes;

import java.util.ArrayList;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.tabs.TabFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.AboutPatchFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.AddFilesFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.DummyFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.MergeFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.RemoveFilesFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.match.AssignFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.match.GotoFragment;
import ru.SnowVolf.pcompiler.ui.fragment.patch.match.ReplaceFragment;

/**
 * Created by radiationx on 02.05.17.
 */

public class MenuItems {
    public final static int ACTION_APP_REGEXP = 0;
    public final static int ACTION_APP_SETTINGS = 1;
    public final static int ACTION_APP_INFO = 2;

    private ArrayList<MenuItem> createdMenuItems = new ArrayList<>();

    public MenuItems() {
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_about), R.drawable.ic_settings_person, AboutPatchFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_match_replace), R.drawable.ic_find_replace, ReplaceFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_match_goto), R.drawable.ic_find_replace, GotoFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_match_assign), R.drawable.ic_find_replace, AssignFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_goto), R.drawable.ic_goto, ru.SnowVolf.pcompiler.ui.fragment.patch.GotoFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_add_files), R.drawable.ic_add_files, AddFilesFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_remove_files), R.drawable.ic_delete_files, RemoveFilesFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_merge), R.drawable.ic_merge, MergeFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.tab_dummy), R.drawable.ic_dummy, DummyFragment.class));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.menu_regexp), R.drawable.ic_regexp, ACTION_APP_REGEXP));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.menu_settings), R.drawable.ic_settings, ACTION_APP_SETTINGS));
        createdMenuItems.add(new MenuItem(App.injectString(R.string.menu_about), R.drawable.ic_info, ACTION_APP_INFO));
    }

    public ArrayList<MenuItem> getCreatedMenuItems() {
        return createdMenuItems;
    }

    public class MenuItem {
        private String title;
        private int iconRes;
        private String attachedTabTag = "";
        private Class<? extends TabFragment> tabClass;
        private int action;
        private boolean active = false;

        public MenuItem(String title, @DrawableRes int iconRes, Class<? extends TabFragment> tabClass) {
            this.title = title;
            this.iconRes = iconRes;
            this.tabClass = tabClass;
        }

        public MenuItem(String title, @DrawableRes int iconRes, int action) {
            this.title = title;
            this.iconRes = iconRes;
            this.action = action;
        }

        public String getTitle() {
            return title;
        }

        public int getIconRes() {
            return iconRes;
        }

        public String getAttachedTabTag() {
            return attachedTabTag;
        }

        public int getAction() {
            return action;
        }

        public Class<? extends TabFragment> getTabClass() {
            return tabClass;
        }

        public void setAttachedTabTag(String attachedTabTag) {
            this.attachedTabTag = attachedTabTag;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}

package ru.SnowVolf.pcompiler.tabs;

/**
 * Created by Snow Volf on 21.10.2017, 15:34
 */

public class TabConfiguration {
    private boolean isAlone = false;
    private boolean isMenu = false;
    private boolean useCache = false;
    private String defaultTitle = "";

    public boolean isAlone() {
        return isAlone;
    }

    public void setAlone(boolean alone) {
        isAlone = alone;
    }

    public boolean isMenu() {
        return isMenu;
    }

    public void setMenu(boolean menu) {
        isMenu = menu;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    @Override
    public String toString() {
        return "TabConfiguration {isAlone = " + isAlone() + ", isMenu = " + isMenu() + ", isUseCache = " + isUseCache() + ", defaultTitle = " + getDefaultTitle() + "}";
    }
}


package ru.SnowVolf.pcompiler.tabs;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.ui.activity.TabbedActivity;
import ru.SnowVolf.pcompiler.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 21.10.2017, 13:03
 */

public class TabFragment extends NativeContainerFragment {
    public final static String ARG_TITLE = "TAB_TITLE";
    public final static String TAB_SUBTITLE = "TAB_SUBTITLE";
    private final static String BUNDLE_PREFIX = "tab_fragment_";
    private final static String BUNDLE_TITLE = "title";
    private final static String BUNDLE_TAB_TITLE = "tab_title";
    private final static String BUNDLE_SUBTITLE = "subtitle";
    private final static String BUNDLE_PARENT_TAG = "parent_tag";


    protected TabConfiguration configuration = new TabConfiguration();

    private String title = null, tabTitle = null, subtitle = null, parentTag = null;

    protected RelativeLayout fragmentContainer;
    protected FrameLayout fragmentContent;
    protected CoordinatorLayout coordinatorLayout;
    protected AppBarLayout appBarLayout;
    protected CollapsingToolbarLayout toolbarLayout;
    private Toolbar toolbar;
    protected TextView toolbarTitleView, toolbarSubtitleView;
    protected View view;

    public TabFragment() {
        parentTag = TabManager.getActiveTag();
    }

    public String getParentTag() {
        return parentTag;
    }

    public TabConfiguration getConfiguration() {
        return configuration;
    }

    /* For TabManager etc */
    public String getTitle() {
        return title == null ? configuration.getDefaultTitle() : title;
    }

    public final void setTitle(String newTitle) {
        this.title = newTitle;
        if (tabTitle == null)
            getTabActivity().updateTabList();
        toolbarTitleView.setText(getTitle());
    }

    protected final String getSubtitle() {
        return subtitle;
    }

    public final void setSubtitle(String newSubtitle) {
        this.subtitle = newSubtitle;
        if (subtitle == null) {
            if (toolbarSubtitleView.getVisibility() != View.GONE)
                toolbarSubtitleView.setVisibility(View.GONE);
        } else {
            if (toolbarSubtitleView.getVisibility() != View.VISIBLE)
                toolbarSubtitleView.setVisibility(View.VISIBLE);
            toolbarSubtitleView.setText(getSubtitle());
        }
    }

    public String getTabTitle() {
        return tabTitle == null ? getTitle() : tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
        getTabActivity().updateTabList();
    }

    public Menu getMenu() {
        return toolbar.getMenu();
    }


    //False - можно закрывать
    //True - еще нужно что-то сделать, не закрывать
    public boolean onBackPressed() {
        Log.d(Constants.TAG, "onbackpressed tab");
        return false;
    }

    public void hidePopupWindows() {
        getTabActivity().hideKeyboard();
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            title = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_TITLE));
            subtitle = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_SUBTITLE));
            tabTitle = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_TAB_TITLE));
            parentTag = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_PARENT_TAG));
        }
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            subtitle = getArguments().getString(TAB_SUBTITLE);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base, container, false);
        //Осторожно! Чувствительно к структуре разметки! (по идеи так должно работать чуть быстрее)
        fragmentContainer = rootView.findViewById(R.id.fragment_container);
        coordinatorLayout = fragmentContainer.findViewById(R.id.coordinator_layout);
        appBarLayout = coordinatorLayout.findViewById(R.id.appbar_layout);
        toolbarLayout = appBarLayout.findViewById(R.id.toolbar_layout);
        toolbar = toolbarLayout.findViewById(R.id.toolbar);
        toolbarTitleView = toolbar.findViewById(R.id.toolbar_title);
        toolbarSubtitleView = toolbar.findViewById(R.id.toolbar_subtitle);
        fragmentContent = coordinatorLayout.findViewById(R.id.fragment_content);
        //bottomBar = rootView.findViewById(R.id.bottom_bar);
        toolbar.setNavigationOnClickListener(configuration.isAlone() || configuration.isMenu() ? getTabActivity().getToggleListener() : getTabActivity().getRemoveTabListener());
        toolbar.setNavigationIcon(configuration.isAlone() || configuration.isMenu() ? R.drawable.ic_menu_hamburger : R.drawable.ic_arrow_back);

        //Для обновления вьюх
        setTitle(title);
        setSubtitle(subtitle);
        return view;
    }

    protected void baseInflateFragment(LayoutInflater inflater, @LayoutRes int res) {
        inflater.inflate(res, fragmentContent, true);
    }

    protected void viewsReady() {
        addBaseToolbarMenu();
    }

    protected void addBaseToolbarMenu() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(Constants.TAG, "onactivitycreated args = " + getArguments() + " : savedInstance = " + savedInstanceState + " : title = " + title);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_PREFIX.concat(BUNDLE_TITLE), title);
        outState.putString(BUNDLE_PREFIX.concat(BUNDLE_SUBTITLE), subtitle);
        outState.putString(BUNDLE_PREFIX.concat(BUNDLE_TAB_TITLE), tabTitle);
        outState.putString(BUNDLE_PREFIX.concat(BUNDLE_PARENT_TAG), parentTag);
    }


    public final TabbedActivity getTabActivity() {
        return (TabbedActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.TAG, this + " : onresume");
    }

    @Override
    public void onPause() {
        super.onPause();
        hidePopupWindows();
        Log.d(Constants.TAG, this + " : onpause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePopupWindows();
    }



    /* Experiment */
    public static class Builder<T extends TabFragment> {
        private T tClass;

        public Builder(Class<T> tClass) {
            try {
                this.tClass = tClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Builder setArgs(Bundle args) {
            tClass.setArguments(args);
            return this;
        }

        public Builder setIsMenu() {
            tClass.configuration.setMenu(true);
            return this;
        }

        /*public Builder setTitle(String title) {
            tClass.setTitle(title);
            return this;
        }*/

        public T build() {
            return tClass;
        }
    }
}


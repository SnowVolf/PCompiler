package ru.SnowVolf.girl.nativex;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Created by Snow Volf on 24.09.2017, 4:41
 */

public class NativeDialog extends Dialog implements AppCompatCallback {

    private AppCompatDelegate mDelegate;

    public NativeDialog(Context context) {
        this(context, 0);
    }

    public NativeDialog(Context context, int theme) {
        super(context, getThemeResId(context, theme));

        // This is a bit weird, but Dialog's are typically created and setup before being shown,
        // which means that we can't rely on onCreate() being called before a content view is set.
        // To workaround this, we call onCreate(null) in the ctor, and then again as usual in
        // onCreate().
        getDelegate().onCreate(null);

        // Apply AppCompat's DayNight resources if needed
        getDelegate().applyDayNight();
    }

    protected NativeDialog(Context context, boolean cancelable,
                              OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        super.onCreate(savedInstanceState);
        getDelegate().onCreate(savedInstanceState);
    }

    /**
     * Support library version of {@link android.app.Dialog#getActionBar}.
     *
     * <p>Retrieve a reference to this dialog's ActionBar.
     *
     * @return The Dialog's ActionBar, or null if it does not have one.
     */
    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    @Nullable
    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        return getDelegate().findViewById(id);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getDelegate().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        getDelegate().setTitle(getContext().getString(titleId));
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    /**
     * Enable extended support library window features.
     * <p>
     * This is a convenience for calling
     * {@link android.view.Window#requestFeature getWindow().requestFeature()}.
     * </p>
     *
     * @param featureId The desired feature as defined in {@link android.view.Window} or
     *                  {@link android.support.v4.view.WindowCompat}.
     * @return Returns true if the requested feature is supported and now enabled.
     *
     * @see android.app.Dialog#requestWindowFeature
     * @see android.view.Window#requestFeature
     */
    public boolean supportRequestWindowFeature(int featureId) {
        return getDelegate().requestWindowFeature(featureId);
    }

    /**
     * @hide
     */
    @Override
    @RestrictTo(LIBRARY_GROUP)
    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    /**
     * @return The {@link AppCompatDelegate} being used by this Dialog.
     */
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, this);
        }
        return mDelegate;
    }

    private static int getThemeResId(Context context, int themeId) {
        if (themeId == 0) {
            // If the provided theme is 0, then retrieve the dialogTheme from our theme
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.dialogTheme, outValue, true);
            themeId = outValue.resourceId;
        }
        return themeId;
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}


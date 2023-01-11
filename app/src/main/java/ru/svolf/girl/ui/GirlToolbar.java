package ru.svolf.girl.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import ru.svolf.pcompiler.R;

/**
 * Created by Snow Volf on 07.11.2017, 16:40
 */

public class GirlToolbar extends Toolbar {
    public GirlToolbar(Context context) {
        super(context);
    }

    public GirlToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GirlToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public View getTabIndicator() {
        return getMenu().getItem(1).getActionView();
    }

    public void setTabIndicatorValue(int value){
        final TextView textView = getTabIndicator().findViewById(R.id.drawer_tabs_count);
        textView.setText(String.format(Locale.ENGLISH, "%d", value));
    }
}

package ru.SnowVolf.pcompiler.ui.fragment.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.util.ThemeWrapper;

/**
 * Created by Snow Volf on 26.08.2017, 21:38
 */

public class SweetContentDialog extends BottomSheetDialog {
    private Context mContext;
    private TextView mContentView;
    private Button mPositive, mNegative;

    public SweetContentDialog(@NonNull Context context) {
        super(context, ThemeWrapper.INSTANCE.getTheme());
        mContext = context;
        initContentView();
    }

    private void initContentView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_simple_content, null);
        mContentView = view.findViewById(R.id.content);
        mPositive = view.findViewById(R.id.positive);
        mNegative = view.findViewById(R.id.negative);
        setContentView(view);
    }

    public void setContentText(int resId){
        mContentView.setText(resId);
    }

    public void setContentText(StringBuilder sb){
        mContentView.setText(sb);
    }

    public void setPositive(CharSequence text, View.OnClickListener listener){
        mPositive.setText(text);
        mPositive.setOnClickListener(listener);
    }

    public void setNegative(CharSequence text, View.OnClickListener listener){
        mNegative.setText(text);
        mNegative.setOnClickListener(listener);
    }

    public void setPositive(int resId, View.OnClickListener listener){
        mPositive.setText(resId);
        mPositive.setOnClickListener(listener);
    }

    public void setNegative(int resId, View.OnClickListener listener){
        mNegative.setText(resId);
        mNegative.setOnClickListener(listener);
    }

    @Override
    public void show() {
        super.show();
        mNegative.setVisibility(mNegative.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void setTypeface(String assetPath){
        final Typeface typeface = Typeface.createFromAsset(App.getContext().getAssets(), assetPath);
        mContentView.setTypeface(typeface);
    }

    public void setTypeface(@FontRes int resId){
        final Typeface typeface = ResourcesCompat.getFont(mContext, resId);
        mContentView.setTypeface(typeface);
    }
}

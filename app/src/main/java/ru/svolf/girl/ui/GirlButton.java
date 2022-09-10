package ru.svolf.girl.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatButton;

import ru.svolf.pcompiler.R;

/**
 * Created by Snow Volf on 04.11.2017, 21:55
 */

public class GirlButton extends AppCompatButton {
    private String mRealText;

    public GirlButton(Context context) {
        super(context);
        init();
    }

    public GirlButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GirlButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GirlButton, defStyleAttr, 0);
        mRealText = ta.getString(R.styleable.GirlButton_insert);
        ta.recycle();
        init();
    }

    private void init(){
        if (mRealText == null || mRealText.isEmpty())
            mRealText = getText().toString();
        setOnLongClickListener(view -> {
            Toast.makeText(getContext(), mRealText, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    public String getRealText(){
        if (mRealText == null){
            mRealText = "";
        }
        return mRealText;
    }

    public void setRealText(String text){
        mRealText = text;
    }

    public void setRealText(@StringRes int resId){
        mRealText = getContext().getString(resId);
    }
}

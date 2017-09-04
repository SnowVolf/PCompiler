package ru.SnowVolf.pcompiler.ui.fragment.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.util.ThemeWrapper;

/**
 * Created by Snow Volf on 20.08.2017, 12:54
 */

public class SweetInputDialog extends BottomSheetDialog {
    private Context mContext;
    private TextView mContentView;
    private TextInputEditText mEditText;
    private Button mPositive, mNegative;

    public SweetInputDialog(@NonNull Context context) {
        super(context, ThemeWrapper.getTheme());
        mContext = context;
        initContentView();
    }

    private void initContentView(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_pref_input, null);
        mContentView = view.findViewById(R.id.title);
        mEditText = view.findViewById(R.id.field_edit);
        mPositive = view.findViewById(R.id.positive);
        //mNegative = view.findViewById(R.id.negative);
        setContentView(view);
    }

    @Override
    public void setTitle(CharSequence title){
        mContentView.setText(title);
    }

    public void setPositive(CharSequence text, View.OnClickListener listener){
        mPositive.setText(text);
        mPositive.setOnClickListener(listener);
    }

//    public void setNegative(CharSequence text, View.OnClickListener listener){
//        mNegative.setText(text);
//        mNegative.setOnClickListener(listener);
//    }

    public String getInputString(){
        return mEditText.getText().toString();
    }

    public TextInputEditText getInputField(){
        return mEditText;
    }

    public void setInputString(String inputString){
        mEditText.setText(inputString);
        mEditText.setSelection(mEditText.getText().length());
    }
}

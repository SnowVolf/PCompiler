package ru.svolf.pcompiler.ui.fragment.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import ru.svolf.girl.ui.GirlEditText;
import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.util.ThemeWrapper;

/**
 * Created by Snow Volf on 20.08.2017, 12:54
 */

public class SweetInputDialog extends BottomSheetDialog {
    private Context mContext;
    private TextView mContentView;
    private GirlEditText mEditText;
    private Button mPositive;

    public SweetInputDialog(@NonNull Context context) {
        super(context, ThemeWrapper.INSTANCE.getTheme());
        mContext = context;
        initContentView();
    }

    private void initContentView(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_pref_input, null);
        mContentView = view.findViewById(R.id.title);
        mEditText = view.findViewById(R.id.field_edit);
        mPositive = view.findViewById(R.id.positive);
        setContentView(view);
    }

    public SweetInputDialog setPrefTitle(CharSequence title){
        mContentView.setText(title);
        return this;
    }

    public SweetInputDialog setPositive(CharSequence text, View.OnClickListener listener){
        mPositive.setText(text);
        mPositive.setOnClickListener(listener);
        return this;
    }

    public String getInputString(){
        return mEditText.getText().toString();
    }

    public GirlEditText getInputField(){
        return mEditText;
    }

    public SweetInputDialog setInputString(String inputString){
        mEditText.setText(inputString);
        mEditText.setSelection(mEditText.getText().length());
        return this;
    }
}

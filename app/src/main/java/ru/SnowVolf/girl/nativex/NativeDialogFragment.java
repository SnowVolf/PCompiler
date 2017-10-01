package ru.SnowVolf.girl.nativex;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.Window;
import android.view.WindowManager;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

public class NativeDialogFragment extends NativeAppDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new NativeDialog(getActivity(), getTheme());
    }

    /** @hide */
    @RestrictTo(LIBRARY_GROUP)
    @Override
    public void setupDialog(Dialog dialog, int style) {
        if (dialog instanceof NativeDialog) {
            // If the dialog is an NativeDialog, we'll handle it
            NativeDialog acd = (NativeDialog) dialog;
            switch (style) {
                case STYLE_NO_INPUT:
                    dialog.getWindow().addFlags(
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    // fall through...
                case STYLE_NO_FRAME:
                case STYLE_NO_TITLE:
                    acd.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            }
        } else {
            // Else, just let super handle it
            super.setupDialog(dialog, style);
        }
    }

}


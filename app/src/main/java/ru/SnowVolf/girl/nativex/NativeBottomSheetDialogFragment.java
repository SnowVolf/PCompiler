package ru.SnowVolf.girl.nativex;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by Snow Volf on 24.09.2017, 4:31
 */

public class NativeBottomSheetDialogFragment extends NativeDialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new NativeBottomSheet(getActivity(), getTheme());
        }
}

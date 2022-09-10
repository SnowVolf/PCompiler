package ru.svolf.pcompiler.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:41
 */

public class NativeContainerFragment extends Fragment {
    public View rootView;

    public NativeContainerFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }
}

package ru.svolf.pcompiler.ui.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:41
 */

public class NativeContainerFragment extends Fragment {
    public View rootView;

    public NativeContainerFragment(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }
}

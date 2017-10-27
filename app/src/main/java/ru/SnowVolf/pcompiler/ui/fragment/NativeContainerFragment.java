package ru.SnowVolf.pcompiler.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by Snow Volf on 17.08.2017, 15:41
 */

public class NativeContainerFragment extends RxFragment {
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

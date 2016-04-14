package com.hamstersapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by CisDevelopment
 * @author Kostya Balyaba
 * on 14.04.2016.
 */

public class AboutDialog extends DialogFragment {

    public static AboutDialog newInstance() {
        return new AboutDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimationSlide);
        getDialog().getWindow().setLayout((int) (((BaseActivity) getActivity()).getScreenWidth() - ((BaseActivity) getActivity()).px2dp(16)),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_about, null);
    }

}

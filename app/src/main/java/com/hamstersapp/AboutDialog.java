package com.hamstersapp;

import android.support.v4.app.DialogFragment;

/**
 * Created by Костя on 14.04.2016.
 */
public class AboutDialog extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimationSlide);
    }
}

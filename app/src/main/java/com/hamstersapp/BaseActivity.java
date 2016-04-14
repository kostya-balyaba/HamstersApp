package com.hamstersapp;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;

/**
 * Created by Костя on 14.04.2016.
 */

public class BaseActivity extends AppCompatActivity {

    public void initToolbar(Toolbar toolbar, boolean arrow) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null && arrow)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addFragment(int id, Fragment frag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, frag);
        fragmentTransaction.commit();
    }

    public void showFragmentDialog(DialogFragment dialog) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "dialog");
    }

    public void share(String title, String url) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(intent,
                getString(R.string.share)));
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics;
    }

    public int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public float px2dp(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

}

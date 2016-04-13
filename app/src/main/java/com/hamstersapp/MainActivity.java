package com.hamstersapp;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(R.id.container, MainFragment.newInstance());
    }

    public void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
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

}

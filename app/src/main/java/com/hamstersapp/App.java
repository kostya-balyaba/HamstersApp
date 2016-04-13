package com.hamstersapp;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 13.04.2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

}

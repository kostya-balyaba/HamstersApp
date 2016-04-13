package com.hamstersapp.data;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.hamstersapp.model.HamsterModel;

import java.util.List;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 13.04.2016.
 */
public class HamstersDataBase {

    private static HamstersDataBase mHamsterDataBase;

    private HamstersDataBase() {
    }

    public static HamstersDataBase getInstance() {
        if (mHamsterDataBase == null)
            mHamsterDataBase = new HamstersDataBase();
        return mHamsterDataBase;
    }


    public List<HamsterModel> getAllHamsters() {
        return new Select().from(HamsterModel.class).orderBy("pinned DESC").execute();
    }

    public synchronized void save(List<HamsterModel> data) {
        if (data != null && !data.isEmpty()) {
            if (isHamstersTableNotEmpty())
                removeAllHamsters();
            try {
                ActiveAndroid.beginTransaction();
                for (HamsterModel model : data) {
                    model.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

    public boolean isHamstersTableNotEmpty() {
        return new Select().from(HamsterModel.class).exists();
    }

    public synchronized void removeAllHamsters() {
        List<HamsterModel> hamstersList = getAllHamsters();
        if (hamstersList != null && !hamstersList.isEmpty()) {
            try {
                ActiveAndroid.beginTransaction();
                for (HamsterModel model : hamstersList) {
                    model.delete();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
    }
}

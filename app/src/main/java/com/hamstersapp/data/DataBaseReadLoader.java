package com.hamstersapp.data;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.hamstersapp.model.HamsterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 13.04.2016.
 */
public class DataBaseReadLoader extends AsyncTaskLoader<List<HamsterModel>> {

    public DataBaseReadLoader(Context context) {
        super(context);
    }

    @Override
    public List<HamsterModel> loadInBackground() {
        return HamstersDataBase.getInstance().getAllHamsters();
    }

}

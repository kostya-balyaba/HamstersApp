package com.hamstersapp.data;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.hamstersapp.model.HamsterModel;

import java.util.List;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 13.04.2016.
 */
public class DataBaseWriteLoader extends AsyncTaskLoader<Void> {

    List<HamsterModel> mData;

    public DataBaseWriteLoader(Context context, List<HamsterModel> data) {
        super(context);
        this.mData = data;
    }

    @Override
    public Void loadInBackground() {
        HamstersDataBase.getInstance().save(mData);
        return null;
    }
}

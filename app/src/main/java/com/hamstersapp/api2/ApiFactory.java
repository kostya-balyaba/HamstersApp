package com.hamstersapp.api2;

import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Костя on 14.04.2016.
 */
public class ApiFactory {


    @NonNull
    public static HamstersService getHamstersService() {
        return getRetrofit().create(HamstersService.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://unrealmojo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}

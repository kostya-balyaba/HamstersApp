package com.hamstersapp.api;

import android.os.Build;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hamstersapp.BuildConfig;

import java.io.IOException;
import java.lang.reflect.Modifier;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 14.04.2016.
 */
public class HamstersApiFactory {

    private static final String BASE_URL = "http://unrealmojo.com/";
    private static OkHttpClient mOkHttpClient;
    private static Gson gson;

    static {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("X-Homo-Client-OS", Build.VERSION.RELEASE)
                        .header("X-Homo-Client-Version", String.format("%s %s", "HamstersApp", BuildConfig.VERSION_NAME))
                        .header("X-Homo-Client-Model", String.format("%s %s", Build.MANUFACTURER, Build.MODEL))
                        .build();
                return chain.proceed(request);
            }
        });
        if (mOkHttpClient == null)
            mOkHttpClient = httpClient.build();

        gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
    }

    @NonNull
    public static HamstersService getHamstersService() {
        return getRetrofit().create(HamstersService.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}

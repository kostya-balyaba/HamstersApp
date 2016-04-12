package com.hamstersapp.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.hamstersapp.BuildConfig;
import com.hamstersapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 12.04.2016.
 */
public abstract class BaseRequest {

    private OkHttpClient mClient;
    private Request mRequest;
    private Context mContext;
    private RequestResultCallback mCallback;
    private String mUrl;
    private static final int TIME_OUT = 20;

    public BaseRequest(Context context, RequestResultCallback callback, String url) {
        this.mContext = context;
        this.mCallback = callback;
        this.mUrl = url;
        buildClient();
        buildRequest();
    }

    public void execute() {
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (mCallback != null)
                    mCallback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (mCallback != null)
                    mCallback.onResponse(response);
            }
        });
    }

    private void buildClient() {
        if (mClient == null)
            mClient = new OkHttpClient();
        mClient.setConnectTimeout(TIME_OUT, TimeUnit.SECONDS);
        mClient.setReadTimeout(TIME_OUT, TimeUnit.SECONDS);
    }

    private void buildRequest() {
        if (mRequest == null)
            mRequest = new Request.Builder()
                    .get()
                    .url(mUrl)
                    .addHeader("X-Homo-Client-OS", Build.VERSION.RELEASE)
                    .addHeader("X-Homo-Client-Version", String.format("%s %s", mContext.getResources().getString(R.string.app_name), BuildConfig.VERSION_NAME))
                    .addHeader("X-Homo-Client-Model", Build.MODEL)
                    .build();

        Log.d("asd", "X-Homo-Client-OS " + Build.VERSION.RELEASE);
        Log.d("asd", "X-Homo-Client-Version " + String.format("%s %s", mContext.getResources().getString(R.string.app_name), BuildConfig.VERSION_NAME));
        Log.d("asd", "X-Homo-Client-Model " + Build.MODEL);
    }

}

package com.hamstersapp.api;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 12.04.2016.
 */
public abstract class BaseResponse<T> implements RequestResultCallback {

    public abstract T parseData(String response);

    @Override
    public void onResponse(Response response) {
        try {
            parseData(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Request request, IOException e) {

    }

}

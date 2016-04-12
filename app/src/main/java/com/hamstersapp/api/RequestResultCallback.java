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
public interface RequestResultCallback {

    void onResponse(Response response);

    void onFailure(Request request, IOException e);

}

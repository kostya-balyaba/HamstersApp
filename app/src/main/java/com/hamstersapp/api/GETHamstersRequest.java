package com.hamstersapp.api;

import android.content.Context;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 12.04.2016.
 */
public class GETHamstersRequest extends BaseRequest {

    private final static String URL = "http://unrealmojo.com/porn/test3";

    public GETHamstersRequest(Context context, RequestResultCallback callback) {
        super(context, callback, URL);
    }

}

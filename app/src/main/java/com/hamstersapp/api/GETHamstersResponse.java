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
public class GETHamstersResponse extends BaseResponse {

    String result;

    @Override
    public void parseData(String response) {

    }

    public String getResult() {
        return result;
    }

}

package com.hamstersapp.api;

import com.google.gson.Gson;
import com.hamstersapp.model.HamsterModel;

import java.util.List;

/**
 * Created by CisDevelopment
 *
 * @author Kostya Balyaba
 *         on 12.04.2016.
 */
public class GETHamstersResponse {

    private String requestResult;

    public GETHamstersResponse(String requestResult) {
        this.requestResult = requestResult;
    }

}

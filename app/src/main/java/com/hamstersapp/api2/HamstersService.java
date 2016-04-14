package com.hamstersapp.api2;

import com.hamstersapp.model.HamsterModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Костя on 14.04.2016.
 */
public interface HamstersService {

    @GET("porn/test3")
    Call<List<HamsterModel>> getHamsters();

}

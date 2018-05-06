package com.application.upstox.api;

import com.application.upstox.model.InitialResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by harsh.jain on 5/6/18.
 */

public interface ApiInterface {

    @GET("api")
    Call<InitialResponse> check();

    @GET("api/historical")
    Call<List<String>> getHistoricalData(@Query("interval") int interval);

}

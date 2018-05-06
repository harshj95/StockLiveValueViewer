package com.application.upstox.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by harsh.jain on 5/6/18.
 */

public class ApiClient {

    private static Retrofit retrofit;

    private ApiClient() {
    }

    public static final String BASE_URL = "http://kaboom.rksv.net/";

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface getApiInterface() {
        return getClient().create(ApiInterface.class);
    }
}

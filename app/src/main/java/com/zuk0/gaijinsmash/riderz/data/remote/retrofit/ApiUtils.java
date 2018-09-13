package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

/*
    NOTE: Use this class to create a retrofit client

 */

import com.zuk0.gaijinsmash.riderz.BuildConfig;

import retrofit2.Retrofit;


public class ApiUtils {

    private static final String BASE_URL = "https://api.bart.gov/api/";

    public static RetrofitInterface getBartApiService() {
        return RetrofitClient.getClient(BASE_URL, new ApiKeyInterceptor(BuildConfig.BartApiKey)).create(RetrofitInterface.class);
    }

    public static Retrofit getBartApiClient() {
        return RetrofitClient.getClient(BASE_URL, new ApiKeyInterceptor(BuildConfig.BartApiKey));
    }
}

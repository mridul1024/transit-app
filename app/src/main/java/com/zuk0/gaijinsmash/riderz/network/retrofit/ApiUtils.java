package com.zuk0.gaijinsmash.riderz.network.retrofit;

/*
    NOTE: Use this class to create a retrofit client

 */

import retrofit2.Retrofit;

public class ApiUtils {

    //TODO: abstract key to build.gradle
    private static final String BART_API_KEY = "Q7Z9-PZ53-9QXT-DWE9";
    private static final String BASE_URL = "http://api.bart.gov/api/";

    public static RetrofitService getBartApiService() {
        return RetrofitClient.getClient(BASE_URL, new ApiKeyInterceptor(BART_API_KEY)).create(RetrofitService.class);
    }

    public static Retrofit getBartApiClient() {
        return RetrofitClient.getClient(BASE_URL, new ApiKeyInterceptor(BART_API_KEY));
    }
}

package com.zuk0.gaijinsmash.riderz.data.remote.network.retrofit;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    private String mApiKey;

    public ApiKeyInterceptor(String apiKey) {
        mApiKey = apiKey;
    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        HttpUrl url = chain.request().url()
                .newBuilder()
                .addQueryParameter("key", mApiKey)
                .build();
        Request request = chain.request().newBuilder().url(url).build();
        return chain.proceed(request);
    }
}

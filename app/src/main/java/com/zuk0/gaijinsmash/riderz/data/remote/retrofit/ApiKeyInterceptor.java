package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    private String mApiKey;
    private String mName;

    public ApiKeyInterceptor(String name, String apiKey) {
        mName = name;
        mApiKey = apiKey;
    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        HttpUrl url = chain.request().url()
                .newBuilder()
                .addQueryParameter(mName, mApiKey)
                .build();
        Request request = chain.request().newBuilder().url(url).build();
        return chain.proceed(request);
    }
}

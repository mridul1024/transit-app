package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JsonInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl url = chain.request().url()
                .newBuilder()
                .addQueryParameter("json","y")
                .build();
        Request request = chain.request().newBuilder().url(url).build();
        return chain.proceed(request);
    }
}

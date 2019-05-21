package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, Interceptor interceptor, GsonConverterFactory gsonFactory) {
        if(retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // For debugging
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);
            builder.interceptors().add(interceptor);
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(gsonFactory)
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}

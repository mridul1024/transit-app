package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class RetrofitClient {

    /*
       Retrofit should be created with the Singleton pattern.
       This client can be used for the whole android project to make requests.
    */

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, Interceptor interceptor) {
        if(retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // For debugging
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);
            builder.interceptors().add(interceptor);
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(new RetrofitConverterFactory())
                    .client(client)
                    .build();
        }
        return retrofit;
    }


}

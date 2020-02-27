package com.zuk0.gaijinsmash.riderz.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WeatherRetrofitClient
constructor(var retrofit: Retrofit) {

    fun getClient(baseUrl: String?, interceptor: Interceptor?, gsonFactory: GsonConverterFactory?): Retrofit? {
        if (retrofit == null) {
            val builder = OkHttpClient.Builder()
            // For debugging
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.networkInterceptors().add(httpLoggingInterceptor)
            builder.interceptors().add(interceptor!!)
            builder.connectTimeout(10, TimeUnit.SECONDS)
            builder.readTimeout(10, TimeUnit.SECONDS)
            val client = builder.build()
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(gsonFactory)
                    .client(client)
                    .build()
        }
        return retrofit
    }
}
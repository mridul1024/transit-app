package com.zuk0.gaijinsmash.riderz.data.remote.retrofit

import java.util.concurrent.TimeUnit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object BartRetrofitClient {

    /*
       Retrofit should be created with the Singleton pattern.
       This client can be used for the whole android project to make requests.
    */

    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String, interceptor: Interceptor): Retrofit? {
        if (retrofit == null) {
            val builder = OkHttpClient.Builder()

            // For debugging
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.networkInterceptors().add(httpLoggingInterceptor)
            builder.interceptors().add(interceptor)
            builder.connectTimeout(10, TimeUnit.SECONDS)
            builder.readTimeout(10, TimeUnit.SECONDS)
            val client = builder.build()

            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(RetrofitConverterFactory())
                    .client(client)
                    .build()
        }
        return retrofit
    }
}

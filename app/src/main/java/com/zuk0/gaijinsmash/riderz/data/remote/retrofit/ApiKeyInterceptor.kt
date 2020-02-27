package com.zuk0.gaijinsmash.riderz.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor(private val mName: String, private val mApiKey: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
                .newBuilder()
                .addQueryParameter(mName, mApiKey)
                .build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }

}
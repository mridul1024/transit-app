package com.zuk0.gaijinsmash.riderz.data.remote.retrofit


import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

import javax.inject.Inject

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


class RetrofitConverterFactory
@Inject constructor(): Converter.Factory() {

    private val gsonFactory = GsonConverterFactory.create(provideGson())
    private val xmlFactory = SimpleXmlConverterFactory.create() //todo - remove

    private fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    override fun responseBodyConverter(
            type: Type?, annotations: Array<Annotation>, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            if (annotation is ResponseTypeConverter.Xml) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit)
            }
            if (annotation is ResponseTypeConverter.Json) {
                return gsonFactory.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null // there is no annotation so we cannot handle it
    }
}

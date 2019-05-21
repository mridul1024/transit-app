package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class RetrofitConverterFactory extends Converter.Factory {

    private final Converter.Factory xmlFactory = SimpleXmlConverterFactory.create();
    private final Converter.Factory gsonFactory = GsonConverterFactory.create();

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if(annotation instanceof ResponseTypeConverter.Xml) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit);
            }
            if(annotation instanceof ResponseTypeConverter.Json) {
                return gsonFactory.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return null; // there is no annotation so we cannot handle it
    }
}

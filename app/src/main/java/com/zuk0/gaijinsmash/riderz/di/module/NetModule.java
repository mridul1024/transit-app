package com.zuk0.gaijinsmash.riderz.di.module;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zuk0.gaijinsmash.riderz.BuildConfig;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.ApiKeyInterceptor;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartRetrofitClient;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.WeatherRetrofitClient;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.WeatherService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// This module will inject all network dependencies and include it into Dagger2's graph

@Module
public class NetModule {
    private static final String WEATHER_API_KEY = BuildConfig.OpenWeatherApiKey;
    private static final String BART_API_KEY = BuildConfig.BartApiKey;
    private static final String BART_BASE_URL = "https://api.bart.gov/api/";
    private static final String WEATHER_BASE_URL = "https://api.openweathermap.org/";

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Singleton
    @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Named("bart")
    @Provides
    @Singleton
    Retrofit provideBartRetrofit() {
        return BartRetrofitClient.getClient(BART_BASE_URL, new ApiKeyInterceptor("key", BART_API_KEY));
    }

    @Named("weather")
    @Provides
    @Singleton
    Retrofit provideWeatherRetrofit(GsonConverterFactory factory) {
        return WeatherRetrofitClient.getClient(WEATHER_BASE_URL, new ApiKeyInterceptor("appid", WEATHER_API_KEY), factory);
    }

    @Provides
    @Singleton
    BartService provideBartInterface(@Named("bart") Retrofit retrofit) {
        return retrofit.create(BartService.class);
    }

    @Provides
    @Singleton
    WeatherService provideWeatherService(@Named("weather") Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }
}

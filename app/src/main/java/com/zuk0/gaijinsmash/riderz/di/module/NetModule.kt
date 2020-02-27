package com.zuk0.gaijinsmash.riderz.di.module

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zuk0.gaijinsmash.riderz.BuildConfig
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.ApiKeyInterceptor
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitConverterFactory
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.WeatherService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

// This module will inject all network dependencies and include it into Dagger2's graph
@Module
class NetModule {

    @Provides
    @Singleton
    fun provideBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)
        return client.build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideRetrofitConverterFactory(): RetrofitConverterFactory {
        return RetrofitConverterFactory()
    }

    @Named("bart")
    @Provides
    @Singleton
    fun provideBartRetrofit(builder: OkHttpClient.Builder, factory: RetrofitConverterFactory): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val interceptor = ApiKeyInterceptor("key", BART_API_KEY)
        builder.networkInterceptors().add(httpLoggingInterceptor)
        builder.interceptors().add(interceptor)
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(10, TimeUnit.SECONDS)
        val client = builder.build()
        return Retrofit.Builder()
                .baseUrl(BART_BASE_URL)
                .addConverterFactory(factory)
                .client(client)
                .build()
    }

    @Named("weather")
    @Provides
    @Singleton
    fun provideWeatherRetrofit(builder: OkHttpClient.Builder, factory: GsonConverterFactory): Retrofit {
        val interceptor = ApiKeyInterceptor("appid", WEATHER_API_KEY)
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        builder.networkInterceptors().add(httpLoggingInterceptor)
        builder.interceptors().add(interceptor)
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(10, TimeUnit.SECONDS)
        val client = builder.build()
        return Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(factory)
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideBartInterface(@Named("bart") retrofit: Retrofit): BartService {
        return retrofit.create(BartService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherService(@Named("weather") retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    companion object {
        private const val WEATHER_API_KEY = BuildConfig.OpenWeatherApiKey
        private const val BART_API_KEY = BuildConfig.BartApiKey
        private const val BART_BASE_URL = "https://api.bart.gov/api/"
        private const val WEATHER_BASE_URL = "https://api.openweathermap.org/"
    }
}
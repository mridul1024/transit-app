package com.zuk0.gaijinsmash.riderz.data.remote.retrofit

import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherService {
    @GET("data/2.5/weather")
    fun getWeatherByName(
            @Query("q") cityName: String?): Call<WeatherResponse>

    @GET("data/2.5/weather")
    fun getWeatherByCityID(
            @Query("id") id: String?): Call<WeatherResponse>

    @GET("data/2.5/weather")
    fun getWeatherByGeoloc(
            @Query("lat") latitude: Double?,
            @Query("lon") longitude: Double?): Call<WeatherResponse>

    @GET("data/2.5/weather")
    fun getWeatherByZipCode(
            @Query("zip") zipcode: Int): Call<WeatherResponse>
}
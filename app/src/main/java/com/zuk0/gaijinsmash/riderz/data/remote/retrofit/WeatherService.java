package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Singleton
public interface WeatherService {

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherByName(
            @Query("q") String cityName);

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherByCityID(
            @Query("id") String id);

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherByGeoloc(
            @Query("lat") Double latitude,
            @Query("lon") Double longitude);

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherByZipCode(
            @Query("zip") int zipcode);
}

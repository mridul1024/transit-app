package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.GET;

@Singleton
public interface WeatherService {
    @JsonAndXmlConverters.Json
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather();

    //todo: api not being called correctly
    //https://api.openweathermap.org/data/2.5/weather?q=london&appid=f819deff72341e3b1daaba2cabe16880


    //http://maps.openweathermap.org/maps/2.0/weather/{op}/{z}/{x}/{y}
    //todo add geolocation parameters
    //api.openweathermap.org/data/2.5/weather?q={city name}
}

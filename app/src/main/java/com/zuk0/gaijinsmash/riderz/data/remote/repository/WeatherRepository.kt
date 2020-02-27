package com.zuk0.gaijinsmash.riderz.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.Coordinates
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.Weather
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.WeatherService
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WeatherRepository
@Inject constructor(val service: WeatherService) {

    // todo get user location - check permissions
    // get user settings - use user location or use the users preferred location.
    // get user location based on lat/long,

    fun getWeatherByGeoloc(lat: Double, long: Double) : LiveData<LiveDataWrapper<WeatherResponse>> {
        val data = MutableLiveData<LiveDataWrapper<WeatherResponse>>()
        service?.getWeatherByGeoloc(lat, long)?.enqueue(object: Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val res = LiveDataWrapper.success<WeatherResponse>(response.body()!!) as LiveDataWrapper<WeatherResponse>
                data.postValue(res)
                if(response.code() == 502) {
                    //todo do something
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("error", t.message)
                val res = LiveDataWrapper.error(null, t.message) as LiveDataWrapper<WeatherResponse>
                data.value = res
            }
        })
        return data
    }

    fun getWeatherByZipcode(zipcode: Int) : LiveData<LiveDataWrapper<WeatherResponse>> {
        val data = MutableLiveData<LiveDataWrapper<WeatherResponse>>()
        service?.getWeatherByZipCode(zipcode)?.enqueue(object: Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val res = LiveDataWrapper.success<WeatherResponse>(response.body()!!) as LiveDataWrapper<WeatherResponse>
                data.postValue(res)
                if(response.code() == 502) {
                    //todo do something
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("error", t.message)
                val res = LiveDataWrapper.error(null, t.message) as LiveDataWrapper<WeatherResponse>
                data.value = res
            }
        })
        return data
    }
}
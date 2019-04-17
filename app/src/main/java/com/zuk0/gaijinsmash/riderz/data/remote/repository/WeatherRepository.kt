package com.zuk0.gaijinsmash.riderz.data.remote.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.Coordinates
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.Weather
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.WeatherService
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository
@Inject constructor(val service: WeatherService) {

    fun getWeather() : MutableLiveData<LiveDataWrapper<WeatherResponse>> {
        val data = MutableLiveData<LiveDataWrapper<WeatherResponse>>()
        service.weather.enqueue(object: Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("error", t.message)
                val res = LiveDataWrapper.error(null, t.message  + "LALALALLALA") as LiveDataWrapper<WeatherResponse>
                data.value = res
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val res = LiveDataWrapper.success(response.body()) as LiveDataWrapper<WeatherResponse>
                data.postValue(res)
                if(response.code() == 502) {
                    //todo do something
                }
            }
        })
        return data
    }
}
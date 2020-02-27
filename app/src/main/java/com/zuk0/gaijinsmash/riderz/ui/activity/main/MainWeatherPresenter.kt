package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
import com.zuk0.gaijinsmash.riderz.databinding.PresenterWeatherBinding
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import kotlin.math.roundToLong

class MainWeatherPresenter(context: Context, val viewModel: MainViewModel, val mainActivityBinding: MainActivityBinding) : LifecycleObserver {

    private val weatherBinding = PresenterWeatherBinding.inflate(LayoutInflater.from(context))

    private val weatherObserver = Observer<LiveDataWrapper<WeatherResponse>> { response ->
        when(response.status) {
            LiveDataWrapper.Status.SUCCESS -> {
                Logger.i("success")
                displayWeather(response.data)
            }
            LiveDataWrapper.Status.ERROR -> {
                response.msg?.let {
                    Logger.e(it)
                }
            }
            else -> {
                Logger.e( "WEATHER ERROR")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        initView()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        //todo restore state
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        //todo save state
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        //todo clean up state
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        viewModel.getWeather()?.removeObserver(weatherObserver)
    }

    private fun initView() {
        Logger.i("initializing weather")
        val container = mainActivityBinding.root.findViewById<FrameLayout>(R.id.widget_weather_container)
        container?.removeView(weatherBinding.root)
        container?.addView(weatherBinding.root)
        viewModel.getWeather()?.observeForever(weatherObserver)
    }

    private fun displayWeather(weather: WeatherResponse) {
        Logger.i("displaying weather")
        var textColor = weatherBinding.root.context.resources.getColor(R.color.white) //default for daytime
        if(!viewModel.isDaytime) textColor = weatherBinding.root.context.resources.getColor(R.color.white)
        if(weather.name != null) { //city
            weatherBinding.weatherNameTv.text = weather.name
            weatherBinding.weatherNameTv.setTextColor(textColor)
        }

        if(weather.main != null) {
            if(weather.main?.temp != null) {
                //° F = 9/5 (K - 273) + 32
                val imperialTemp = viewModel.kelvinToFahrenheit(weather.main?.temp as Double).roundToLong().toString()
                val temp = imperialTemp + weatherBinding.root.context.resources.getString(R.string.weather_temp_imperial)
                weatherBinding.weatherTempTv.text = temp
                weatherBinding.weatherTempTv.setTextColor(textColor) //abstract
            }
        }

        if(weather.main?.humidity != null) {
            val humidity = "${weather.main?.humidity}%"
            weatherBinding.weatherHumidityTv.text = humidity //todo use string resource
            weatherBinding.weatherHumidityTv.setTextColor(textColor)
        }

        if(weather.wind != null) {
            val wind = "${weather.wind?.speed?.toString()}mph"
            weatherBinding.weatherWindTv.text = wind
            weatherBinding.weatherWindTv.setTextColor(textColor)
        }
    }
}
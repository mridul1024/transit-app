package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.util.Log
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
import com.zuk0.gaijinsmash.riderz.utils.CrashLogUtil
import java.util.*
import kotlin.math.roundToLong

class WeatherPresenter(context: Context, val viewModel: MainViewModel, val mainActivityBinding: MainActivityBinding) : LifecycleObserver {

    private val weatherBinding = PresenterWeatherBinding.inflate(LayoutInflater.from(context))

    private val weatherObserver = Observer<LiveDataWrapper<WeatherResponse>> { response ->
        when(response.status) {
            LiveDataWrapper.Status.SUCCESS -> {
                Logger.i("success")
                displayWeather(response.data)
            }
            LiveDataWrapper.Status.ERROR -> {
                response.msg?.let {
                    CrashLogUtil.log(it)
                }
            }
            else -> {
                Logger.e( "WEATHER ERROR")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        viewModel.getLocation() //required for weather
        initView()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        //todo restore state
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        //todo save state
        viewModel.getWeather()?.removeObserver(weatherObserver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        //todo clean up state
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        //todo
    }

    private fun initView() {
        Logger.i("initializing weather")
        val container = mainActivityBinding.root.findViewById<FrameLayout>(R.id.widget_weather_container)
        container?.removeView(weatherBinding.root)
        container?.addView(weatherBinding.root)
        viewModel.getWeather()?.observeForever(weatherObserver)
    }

    //todo need to refactor this - DRY!
    private fun displayWeather(response: WeatherResponse) {
        Logger.i("displaying weather")
        var textColor = weatherBinding.root.context.resources.getColor(R.color.white) //default for daytime

        val weatherCondition = response.weather?.get(0)?.main ?: ""
        if(viewModel.isDuskOrDawn) {
            when (weatherCondition.toLowerCase(Locale.getDefault())) {
                "clear" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_half_sun))
                }
                "clouds" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_cloud))
                }
                "rain" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_rain))
                }
                "wind" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_wind))
                }
                else -> {
                    CrashLogUtil.log("unhandled weather type: $weatherCondition")
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_half_sun))
                }
            }
        } else if(viewModel.isDaytime) {
            when (weatherCondition.toLowerCase(Locale.getDefault())) {
                "clear" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_sun))
                }
                "clouds" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_cloudy_sun))
                }
                "rain" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_rain))
                }
                "wind" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_wind))
                }
                else -> {
                    CrashLogUtil.log("unhandled weather type: $weatherCondition")
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_sun))
                }
            }
        } else {
            textColor = weatherBinding.root.context.resources.getColor(R.color.white)
            when (weatherCondition.toLowerCase(Locale.getDefault())) {
                "clear" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_moon))
                }
                "clouds" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_cloudy_moon))
                }
                "rain" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_rain))
                }
                "wind" -> {
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_wind))
                }
                else -> {
                    CrashLogUtil.log("unhandled weather type: $weatherCondition")
                    weatherBinding.weatherIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_moon))
                }
            }
        }

        if(response.name != null) { //city
            weatherBinding.weatherNameTv.text = response.name
            weatherBinding.weatherNameTv.setTextColor(textColor)
        }

        if(response.main != null) {
            if(response.main?.temp != null) {
                //° F = 9/5 (K - 273) + 32
                val imperialTemp = viewModel.kelvinToFahrenheit(response.main?.temp as Double).roundToLong().toString()

                val metricTemp = viewModel.kelvinToCelcius(response.main?.temp as Double).roundToLong().toString()
                Log.d(TAG, "imperial: $imperialTemp, metric: $metricTemp")

                if(viewModel.isMetricSystemEnabled()) {
                    weatherBinding.weatherTempTv.text = metricTemp
                    weatherBinding.weatherTempTv.setTextColor(textColor) //abstract
                    weatherBinding.weatherTempIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_celcius))
                } else {
                    weatherBinding.weatherTempTv.text = imperialTemp
                    weatherBinding.weatherTempTv.setTextColor(textColor) //abstract
                    weatherBinding.weatherTempIcon.setImageDrawable(weatherBinding.root.context.resources.getDrawable(R.drawable.ic_weather_fahrenheit))
                }
            }
        }

        if(response.main?.humidity != null) {
            val humidity = "${response.main?.humidity}%"
            weatherBinding.weatherHumidityTv.text = humidity //todo use string resource
            weatherBinding.weatherHumidityTv.setTextColor(textColor)
        }

        if(response.wind != null) {
            if(viewModel.isMetricSystemEnabled()) {
                val wind = "${viewModel.formatDoubleValue(viewModel.toKPH(response.wind?.speed))} kph"
                weatherBinding.weatherWindTv.text = wind
                weatherBinding.weatherWindTv.setTextColor(textColor)
            } else {
                val wind = "${response.wind?.speed?.toString()} mph"
                weatherBinding.weatherWindTv.text = wind
                weatherBinding.weatherWindTv.setTextColor(textColor)
            }
        }
    }

    fun refresh() {
        val temp = weatherBinding.weatherTempTv.text
        if(temp.isNotBlank()) {
            //todo refresh views
        }
    }

    companion object {
        private const val TAG = "MainWeatherPresenter"

    }
}
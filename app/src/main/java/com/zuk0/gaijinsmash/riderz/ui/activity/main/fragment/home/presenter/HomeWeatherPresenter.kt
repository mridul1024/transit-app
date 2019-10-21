package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.presenter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.databinding.WidgetWeatherBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import kotlin.math.roundToLong

class HomeWeatherPresenter(context: Context) : LifecycleObserver {


    /**
     * Very Important to do before registering to lifecycle
     */
    var viewModel: MainViewModel? = null

    private val binding = WidgetWeatherBinding.inflate(LayoutInflater.from(context))

    private val weatherObserver = Observer<LiveDataWrapper<WeatherResponse>> { response ->
        when(response.status) {
            LiveDataWrapper.Status.SUCCESS -> {
                Logger.i("success")
                displayWeather(response.data)
            }
            LiveDataWrapper.Status.ERROR -> {
                Logger.e(response.msg)
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
        viewModel?.getWeather()?.removeObserver(weatherObserver)
    }

    private fun initView() {
        Logger.i("initializing weather")
        val mainFrameLayout = binding.root.findViewById<FrameLayout>(R.id.widget_weather)
        mainFrameLayout?.addView(binding.root)
        viewModel?.getWeather()?.observeForever(weatherObserver)
    }

    private fun displayWeather(weather: WeatherResponse) {
        Logger.i("displaying weather")
        var textColor = binding.root.context.resources.getColor(R.color.white) //default for daytime
        if(viewModel?.isDaytime == false) textColor = binding.root.context.resources.getColor(R.color.white)
        if(weather.name != null) { //city
            binding.weatherNameTv.text = weather.name
            binding.weatherNameTv.setTextColor(textColor)
        }

        if(weather.main != null) {
            if(weather.main?.temp != null) {
                //° F = 9/5 (K - 273) + 32
                val imperialTemp = viewModel?.kelvinToFahrenheit(weather.main?.temp as Double)?.roundToLong().toString()
                val temp = imperialTemp + binding.root.context.resources.getString(R.string.weather_temp_imperial)
                binding.weatherTempTv.text = temp
                binding.weatherTempTv.setTextColor(textColor) //abstract
            }
        }

        if(weather.main?.humidity != null) {
            val humidity = "${weather.main?.humidity}%"
            binding.weatherHumidityTv.text = humidity //todo use string resource
            binding.weatherHumidityTv.setTextColor(textColor)
        }

        if(weather.wind != null) {
            val wind = "${weather.wind?.speed?.toString()}mph"
            binding.weatherWindTv.text = wind
            binding.weatherWindTv.setTextColor(textColor)
        }
    }
}
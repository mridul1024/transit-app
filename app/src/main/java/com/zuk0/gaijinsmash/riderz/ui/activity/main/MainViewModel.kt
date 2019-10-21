package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.data.remote.repository.WeatherRepository
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import javax.inject.Singleton

@Singleton
class MainViewModel
    constructor(val weatherRepository: WeatherRepository): ViewModel() {

    private val userLocation: Location? = null

    internal val isDaytime: Boolean
        get() = TimeDateUtils.isDaytime()

    internal val hour: Int
        get() = TimeDateUtils.getCurrentHour()

    fun getLocation(context: Context?) : Location? {
        if(userLocation == null && context != null) {
            val gps = GpsUtils(context)
            gps.location
        }
        return userLocation
    }

    fun getColorScheme(context: Context, hour: Int) : Int {
        var color = 0
        if (hour < 6 || hour >= 21) {
            // show night color
            color = ContextCompat.getColor(context, R.color.white)
        } else if (hour >= 17) {
            // show dusk color
            color = ContextCompat.getColor(context, R.color.white)
        } else {
            // show day color
            color = ContextCompat.getColor(context, R.color.black)
        }
        return color
    }

    fun getBackgroundDrawable(context: Context, hour: Int) : Drawable? {
        val bg: Drawable?
        if (hour < 6 || hour >= 21) {
            // show night picture
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_night)
        } else if (hour >= 17) {
            // show dusk picture
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_dusk)
        } else {
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_day)
        }
        return bg
    }

    /**
     * Save UI state
     */
    fun saveState(outState: Bundle) {
        //todo
    }

    /**
     * Restore UI state
     */
    fun restoreState(savedInstanceState: Bundle) {
        //todo
    }

    /**
     * Weather Stuff
     */
    internal fun getWeather(): LiveData<LiveDataWrapper<WeatherResponse>> {
        userLocation?.let {
            return weatherRepository.getWeatherByGeoloc(it.latitude, it.longitude)
        }
        return weatherRepository.getWeatherByZipcode(94108) //default if userLocation is unavailable
    }

    //F = 9/5 (K - 273) + 32
    internal fun kelvinToFahrenheit(temp: Double): Double {
        return 9f / 5f * (temp - 273) + 32
    }

    //C = K - 273
    internal fun kelvinToCelcius(temp: Double): Double {
        return temp - 273
    }

}
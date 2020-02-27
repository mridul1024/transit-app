package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.data.remote.repository.WeatherRepository
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class MainViewModel
@Inject constructor(val weatherRepository: WeatherRepository): ViewModel() {

    private val userLocation: Location? = null

    val isDaytime: Boolean
        get() = TimeDateUtils.isDaytime

    val isNightTime: Boolean
        get() = TimeDateUtils.isNightTime

    internal val hour: Int
        get() = TimeDateUtils.currentHour

    fun getLocation(context: Context?) : Location? {
        if(userLocation == null && context != null) {
            val gps = LocationManager(context)
            gps.location
        }
        return userLocation
    }

    fun getColorScheme(context: Context, hour: Int) : Int {
        var color = 0
        if (hour < 6 || hour >= 20) {
            // show night color
            color = ContextCompat.getColor(context, R.color.white)
        } else if (hour in 18..19) {
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
    fun getWeather(): LiveData<LiveDataWrapper<WeatherResponse>>? {
        userLocation?.let {
            return weatherRepository?.getWeatherByGeoloc(it.latitude, it.longitude)
        }
        return weatherRepository?.getWeatherByZipcode(94108) //default if userLocation is unavailable
    }

    //F = 9/5 (K - 273) + 32
    fun kelvinToFahrenheit(temp: Double): Double {
        return 9f / 5f * (temp - 273) + 32
    }

    //C = K - 273
    internal fun kelvinToCelcius(temp: Double): Double {
        return temp - 273
    }

    fun initBartMap(context: Context?, imageView: ImageView?) {
        if(context != null && imageView != null) {
            val img: Drawable?
            val cal = Calendar.getInstance(TimeZone.getTimeZone("PST"), Locale.US)
            val day = cal.get(Calendar.DAY_OF_WEEK)
            if (day == 7) {
                img = context.getDrawable(R.drawable.bart_map_sunday)
            } else {
                img = context.getDrawable(R.drawable.bart_map_weekday_sat)
            }
            Glide.with(context)
                    .load(img)
                    .into(imageView)
        }
    }

}
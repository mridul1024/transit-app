package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.data.remote.repository.WeatherRepository
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.settings.SettingsFragment
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class MainViewModel
@Inject constructor(application: Application, val weatherRepository: WeatherRepository): AndroidViewModel(application) {

    private val userLocation: Location? = null

    val isDaytime: Boolean
        get() = TimeDateUtils.isDaytime

    val isNightTime: Boolean
        get() = TimeDateUtils.isNightTime

    val isDuskOrDawn: Boolean
        get() = TimeDateUtils.isDusk or TimeDateUtils.isMorning

    internal val hour: Int
        get() = TimeDateUtils.currentHour

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(getApplication())
    }

    fun isMetricSystemEnabled() : Boolean {
        return sharedPreferences.getBoolean(SettingsFragment.PreferenceKey.METRIC_PREFS.value, false)
    }

    fun getLocation() : Location? {
        if(userLocation == null) {
            val gps = LocationManager(getApplication())
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

    /**
     * This is not rounded and requires string formatting
     * so that you don't print out a value like, 2.3320000000002
     */
    fun toKPH(mph: Double?) : Double {
        mph?.let {
            return (mph * 1.60934)
        }
        return 0.0
    }

    fun formatDoubleValue(value: Double) : String {
        return String.format("%.1f", value)
    }

    fun getWeather(): LiveData<LiveDataWrapper<WeatherResponse>>? {
        userLocation?.let {
            return weatherRepository.getWeatherByGeoloc(it.latitude, it.longitude)
        }
        return weatherRepository.getWeatherByZipcode(DEFAULT_ZIPCODE) //default if userLocation is unavailable
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

    //todo
    fun initUserRatingDialog(context: Context) {
        val dialog = AlertDialog.Builder(context).create()
        //create custom dialog for rating
        //submit rating to
        dialog.show()
    }


    companion object {
        private const val TAG = "MainViewModel"
        private const val DEFAULT_ZIPCODE = 94108 // San Francisco
    }
}
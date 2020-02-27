package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.zuk0.gaijinsmash.riderz.R

object ThemeUtil {

    const val SHARED_PREFS_KEY_DAY_NIGHT_THEME = "KEY_DAY_NIGHT_THEME"

    enum class ThemeOption { LIGHT, DARK, BATTERY_SAVER, SYSTEM_DEFAULT, AUTO }

    /**
     * Checks App's settings first, then system settings
     * https://developer.android.com/guide/topics/ui/look-and-feel/darktheme
     */
    fun isDarkThemeOn(context: Context?): Boolean {
        if (context == null)
            return false

        when(getAppThemeMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                return false
            }
            AppCompatDelegate.MODE_NIGHT_YES ->{
                return true
            }
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> {
                //defer to system settings.
                return isSystemDarkThemeOn(context)
            }
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED -> {
                //defer to system settings.
                return isSystemDarkThemeOn(context)
            }
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                //defer to system settings.
                return isSystemDarkThemeOn(context)
            }
        }
        return false
    }

    private fun isSystemDarkThemeOn(context: Context?) : Boolean {
        if (context == null)
            return false

        //return false  //kill switch
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_MASK -> true
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    private fun getAppThemeMode(): Int {
        return AppCompatDelegate.getDefaultNightMode()
    }

    private fun getSystemThemeMode(context: Context): Int {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

    fun getColorTheme(context: Context) : Drawable? {
        val hour = TimeDateUtils.currentHour
        val bg: Drawable?
        if (hour < 6 || hour >= 20) {
            // show night picture
            //nighttime_light
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_night)
        } else if (hour in 18..19) {
            // show dusk picture
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_dusk)
        } else {
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_day)
        }
        return bg
    }

}
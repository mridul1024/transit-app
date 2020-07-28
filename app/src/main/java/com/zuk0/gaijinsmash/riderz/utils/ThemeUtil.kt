package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.zuk0.gaijinsmash.riderz.R

object ThemeUtil {

    private const val SHARED_PREFS_KEY_DAY_NIGHT_THEME = "SHARED_PREFS_KEY_DAY_NIGHT_THEME"

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

        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_MASK -> true
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    private fun getAppThemeMode(): Int {
        return AppCompatDelegate.getDefaultNightMode()
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

    private fun getSystemThemeMode(context: Context): Int {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

    fun getSavedThemePreference(context: Context?): Int {
        context?.let {
            val defaultValue = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            return PreferenceManager.getDefaultSharedPreferences(context).getInt(SHARED_PREFS_KEY_DAY_NIGHT_THEME, defaultValue)
        }
        return -1
    }

    fun setThemePreference(context: Context?, choice: Int) {
        context?.let {
            val mode: Int
            when(choice) {
                0 -> { //Light
                    mode = AppCompatDelegate.MODE_NIGHT_NO
                }
                1 -> { //Dark
                    mode = AppCompatDelegate.MODE_NIGHT_YES
                }
                2 -> { //System
                    mode = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    else
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                else -> {
                    mode = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
                }
            }
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putInt(SHARED_PREFS_KEY_DAY_NIGHT_THEME, mode)
                    .apply()
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}
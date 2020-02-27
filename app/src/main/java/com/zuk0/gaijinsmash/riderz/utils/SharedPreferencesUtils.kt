package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.settings.SettingsFragment

object SharedPreferencesUtils {

    private const val DEV_UPDATE_PREFS = "DEV_UPDATE_PREFS"
    private const val DEV_UPDATE_KEY = "DEV_UPDATE_KEY"

    fun getTimePreference(context: Context?): Boolean {
        val prefs = context?.getSharedPreferences(SettingsFragment.PreferenceKey.TIME_PREFS.value, Context.MODE_PRIVATE)
        return prefs?.getBoolean(SettingsFragment.PreferenceKey.TIME_KEY.value, false) ?: false
    }

    fun getDevUpdatePreference(context: Context): Boolean {
        val prefs = context.getSharedPreferences(DEV_UPDATE_PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean(DEV_UPDATE_KEY, false)
    }

    fun disableDevUpdatePreference(context: Context) {
        val editor = context.getSharedPreferences(DEV_UPDATE_PREFS, Context.MODE_PRIVATE).edit()
        editor.putBoolean(DEV_UPDATE_KEY, true).apply()
    }

    fun enableDevUpdatePreference(context: Context) {
        val editor = context.getSharedPreferences(DEV_UPDATE_PREFS, Context.MODE_PRIVATE).edit()
        editor.putBoolean(DEV_UPDATE_KEY, false).apply()
    }
}
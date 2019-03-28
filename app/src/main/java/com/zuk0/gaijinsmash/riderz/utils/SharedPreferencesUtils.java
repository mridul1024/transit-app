package com.zuk0.gaijinsmash.riderz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.settings.SettingsFragment;

public class SharedPreferencesUtils {

    private static final String DEV_UPDATE_PREFS = "DEV_UPDATE_PREFS";
    private static final String DEV_UPDATE_KEY = "DEV_UPDATE_KEY";

    public static boolean getTimePreference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SettingsFragment.PreferenceKey.TIME_PREFS.getValue(), Context.MODE_PRIVATE);
        return prefs.getBoolean(SettingsFragment.PreferenceKey.TIME_KEY.getValue(), false);
    }

    public static boolean getDevUpdatePreference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEV_UPDATE_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(DEV_UPDATE_KEY, false);
    }

    public static void disableDevUpdatePreference(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEV_UPDATE_PREFS, Context.MODE_PRIVATE).edit();
        editor.putBoolean(DEV_UPDATE_KEY,true).apply();
    }

    public static void enableDevUpdatePreference(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEV_UPDATE_PREFS, Context.MODE_PRIVATE).edit();
        editor.putBoolean(DEV_UPDATE_KEY, false).apply();
    }
}

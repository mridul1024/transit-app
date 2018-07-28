package com.zuk0.gaijinsmash.riderz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zuk0.gaijinsmash.riderz.ui.fragment.SettingsFragment;

public class SharedPreferencesHelper {

    public static boolean getTimePreference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SettingsFragment.PreferenceKey.TIME_PREFS.getValue(), Context.MODE_PRIVATE);
        return prefs.getBoolean(SettingsFragment.PreferenceKey.TIME_KEY.getValue(), false);
    }
}

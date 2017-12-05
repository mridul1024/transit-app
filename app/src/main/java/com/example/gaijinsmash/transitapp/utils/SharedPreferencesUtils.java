package com.example.gaijinsmash.transitapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtils {

    public static boolean isTwentyFourHrTimeOn(Context context) {
        //TODO: fix the boolean
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("", true);
    }
}

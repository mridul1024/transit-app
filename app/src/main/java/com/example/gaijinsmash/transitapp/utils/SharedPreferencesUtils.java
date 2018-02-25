package com.example.gaijinsmash.transitapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtils {

    // todo: fix this to //
    //              Check SharedPreferences for time setting
    //        SharedPreferences prefs = getActivity().getSharedPreferences("TIME_PREFS", Context.MODE_PRIVATE);
    //        mTimeBoolean = prefs.getBoolean("TIME_KEY", false);
    public static boolean isTwentyFourHrTimeOn(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("", false);
    }
}

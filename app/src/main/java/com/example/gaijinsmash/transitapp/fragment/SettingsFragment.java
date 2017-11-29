package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.gaijinsmash.transitapp.R;

/**
 * SharedPreferences
 */

public class SettingsFragment extends PreferenceFragment {

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_preferences);
    }
}

package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;

/**
 * SharedPreferences
 * Implementation is slightly different with PreferenceFragment
 */

public class SettingsFragment extends PreferenceFragment {

    private CheckBoxPreference mCheckBoxPreference;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_preferences);

        mCheckBoxPreference = (CheckBoxPreference) getPreferenceManager().findPreference("checkbox_preference_key");

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isTwentyFourHrFormatOn = mSharedPreferences.getBoolean("checkbox_time_key", true);
    }
}

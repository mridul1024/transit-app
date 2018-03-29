package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.gaijinsmash.transitapp.R;

/**
 * SharedPreferences
 * Implementation is slightly different with PreferenceFragment
 */

public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    public static String PREFS_NAME = "TIME_PREFS";
    public static String PREFS_KEY = "TIME_KEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_preferences);

        CheckBoxPreference mCheckBoxPreference = (CheckBoxPreference) findPreference(PREFS_KEY);
        mCheckBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("Preference Value Changed to: ", newValue.toString());
                save(getActivity(), (Boolean) newValue);
                return true;
            }
        });
    }

    public void save(Context context, boolean result) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREFS_KEY, result);
        editor.apply();
    }

    public boolean getCheckboxValue(Context context) {
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(PREFS_KEY);
        return checkBoxPreference.isChecked();
    }

    public boolean getValue(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return settings.getBoolean(PREFS_KEY, false);
    }

    public void clearSharedPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }
}

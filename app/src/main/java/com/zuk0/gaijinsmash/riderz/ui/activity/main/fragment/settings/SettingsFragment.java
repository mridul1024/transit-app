package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.zuk0.gaijinsmash.riderz.R;

import java.util.Objects;
//todo: how to setup dagger with this?
public class SettingsFragment extends PreferenceFragmentCompat {

    public enum PreferenceKey {
        TIME_PREFS("TIME_PREFS"), TIME_KEY("TIME_KEY");

        private String value;

        PreferenceKey(String value) {
            this.value = value;
        }
        public String getValue() { return value; }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(PreferenceKey.TIME_KEY.getValue());
        checkBoxPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            save(Objects.requireNonNull(getActivity()), (Boolean) newValue);
            return true;
        });
    }

    private void save(Context context, boolean result) {
        SharedPreferences settings = context.getSharedPreferences(PreferenceKey.TIME_PREFS.getValue(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PreferenceKey.TIME_KEY.getValue(), result);
        editor.apply();
    }

    public boolean getCheckboxValue(Context context) {
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(PreferenceKey.TIME_KEY.getValue());
        return checkBoxPreference.isChecked();
    }

    public boolean getValue(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PreferenceKey.TIME_KEY.getValue(), Context.MODE_PRIVATE);
        return settings.getBoolean(PreferenceKey.TIME_KEY.getValue(), false);
    }

    public void clearSharedPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PreferenceKey.TIME_PREFS.getValue(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }
}

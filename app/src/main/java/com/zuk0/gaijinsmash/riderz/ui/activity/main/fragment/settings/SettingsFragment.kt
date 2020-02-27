package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.zuk0.gaijinsmash.riderz.R
import java.util.*

//todo: how to setup dagger with this?
class SettingsFragment : PreferenceFragmentCompat() {
    enum class PreferenceKey(val value: String) {
        TIME_PREFS("TIME_PREFS"), TIME_KEY("TIME_KEY");

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) { // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)
        val checkBoxPreference = findPreference<Preference>(PreferenceKey.TIME_KEY.value) as CheckBoxPreference?
        checkBoxPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any ->
            save(activity!!, newValue as Boolean)
            true
        }
    }

    private fun save(context: Context, result: Boolean) {
        val settings = context.getSharedPreferences(PreferenceKey.TIME_PREFS.value, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putBoolean(PreferenceKey.TIME_KEY.value, result)
        editor.apply()
    }

    fun getCheckboxValue(context: Context?): Boolean {
        val checkBoxPreference = findPreference<Preference>(PreferenceKey.TIME_KEY.value) as CheckBoxPreference?
        return checkBoxPreference!!.isChecked
    }

    fun getValue(context: Context): Boolean {
        val settings = context.getSharedPreferences(PreferenceKey.TIME_KEY.value, Context.MODE_PRIVATE)
        return settings.getBoolean(PreferenceKey.TIME_KEY.value, false)
    }

    fun clearSharedPreferences(context: Context) {
        val settings = context.getSharedPreferences(PreferenceKey.TIME_PREFS.value, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.clear()
        editor.apply()
    }
}
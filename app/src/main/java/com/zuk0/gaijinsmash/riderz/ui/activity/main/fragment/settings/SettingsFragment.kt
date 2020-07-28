package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.settings

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.utils.ThemeUtil

class SettingsFragment : PreferenceFragmentCompat() {
    enum class PreferenceKey(val value: String) {
        TIME_PREFS("TIME_PREFS"), TIME_KEY("TIME_KEY"),
        METRIC_PREFS("METRIC_PREFS"), METRIC_KEY("METRIC_KEY")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val collapsingToolbarLayout: CollapsingToolbarLayout = it.findViewById(R.id.main_collapsing_toolbar)
            collapsingToolbarLayout.title = getString(R.string.action_settings)
            val appBarLayout: AppBarLayout = it.findViewById(R.id.main_app_bar_layout)
            appBarLayout.setExpanded(false)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) { // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)

        val themePreference = findPreference<Preference>("THEME_KEY")
        val timePreference = findPreference<Preference>("TIME_KEY")
        val metricPreference = findPreference<Preference>("METRIC_KEY")
        val resetPreference = findPreference<Preference>("RESET_KEY")
        val locationPreference = findPreference<Preference>("LOCATION_KEY")

        themePreference?.icon?.setTintList(ResourcesCompat.getColorStateList(resources, R.color.icon_color, context?.theme))
        themePreference?.setOnPreferenceClickListener {
            context?.let {
                val dialog = AlertDialog.Builder(it)
                dialog.setTitle(getString(R.string.alert_dialog_theme_title))

                val choices: Array<String?> = resources.getStringArray(R.array.theme_options)
                val mutChoices: MutableList<String?> = choices.toMutableList()

                val savedChoice = when(ThemeUtil.getSavedThemePreference(context)) {
                    AppCompatDelegate.MODE_NIGHT_NO -> 0
                    AppCompatDelegate.MODE_NIGHT_YES -> 1
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> 2
                    else -> 2
                }
                dialog.setSingleChoiceItems(mutChoices.toTypedArray(), savedChoice) { d , choice ->
                    ThemeUtil.setThemePreference(context, choice)
                    d.dismiss()
                }
                dialog.setNeutralButton(getString(R.string.alert_dialog_cancel)) {
                    d, _ -> d.cancel()
                }
                dialog.create()
                dialog.show()
            }
            true
        }

        timePreference?.icon?.setTintList(ResourcesCompat.getColorStateList(resources, R.color.icon_color, context?.theme))
        timePreference?.setOnPreferenceChangeListener { preference, newValue ->
            context?.let {
                save(it, PreferenceKey.TIME_PREFS.value, newValue as Boolean)
            }
            true
        }
        metricPreference?.icon?.setTintList(ResourcesCompat.getColorStateList(resources, R.color.icon_color, context?.theme))
        metricPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any ->
            activity?.let {
                save(it, PreferenceKey.METRIC_PREFS.value, newValue as Boolean)
            }
            true
        }
        locationPreference?.icon?.setTintList(ResourcesCompat.getColorStateList(resources, R.color.icon_color, context?.theme))
        locationPreference?.setOnPreferenceClickListener {
            val openAppSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity?.packageName, null)
            openAppSettingsIntent.data = uri
            startActivity(openAppSettingsIntent)
            true
        }

        resetPreference?.icon?.setTintList(ResourcesCompat.getColorStateList(resources, R.color.icon_color, context?.theme))
        resetPreference?.let {
            it.setOnPreferenceClickListener { pref ->
                val dialog = AlertDialog.Builder(context).create()
                dialog.setTitle(getString(R.string.preference_reset_dialog_title))
                dialog.setMessage(getString(R.string.preference_reset_dialog_message))
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_dialog_yes)) { d, _ ->
                    clearSharedPreferences(context)
                    d.dismiss()
                }
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_dialog_no)) { d, _ ->
                    d.cancel()
                }
                dialog.show()
                false
            }
        }
    }

    private fun save(context: Context?, key: String, value: Boolean) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor?.putBoolean(key, value)
                ?.apply()
    }

    fun getCheckboxValue(context: Context?, key: String): Boolean {
        val checkBoxPreference = findPreference<Preference>(key) as CheckBoxPreference?
        return checkBoxPreference?.isChecked == true
    }

    fun getValue(context: Context?, key: String): Boolean {
        context?.let {
            val settings = PreferenceManager.getDefaultSharedPreferences(context)
            return settings.getBoolean(key, false)
        }
        return false
    }

    private fun clearSharedPreferences(context: Context?) {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        settings?.edit()
                ?.clear()
                ?.apply()
    }
}
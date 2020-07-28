package com.zuk0.gaijinsmash.riderz.ui.activity.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.utils.CrashLogUtil
import com.zuk0.gaijinsmash.riderz.utils.ThemeUtil
import com.zuk0.gaijinsmash.riderz.utils.xml_parser.StationXmlParser

import org.xmlpull.v1.XmlPullParserException

import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference

import javax.inject.Inject
import javax.inject.Singleton

class SplashViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    fun initStationsData() {
        SaveStationsTask(super.getApplication()).execute()
    }

    private class SaveStationsTask internal constructor(application: Application) : AsyncTask<Void, Void, Void>() {
        private val mWeakRef: WeakReference<Application> = WeakReference(application)

        private val count: Int
            get() {
                val count = StationDatabase.getRoomDB(mWeakRef.get()!!)
                        .stationDao()
                        .countStations()
                Log.i("doInBackground", count.toString())
                return count as Int
            }

        private val list: List<Station>?
            get() {
                val `is`: InputStream
                var stationList: List<Station>? = null
                try {
                    `is` = mWeakRef.get()?.assets?.open("stations.xml")!!
                    val parser = StationXmlParser(`is`)
                    stationList = parser.list
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: XmlPullParserException) {
                    e.printStackTrace()
                }

                return stationList
            }

        override fun doInBackground(vararg voids: Void): Void? {
            val count = count
            if (count < BART_STATIONS_COUNT) {
                val stationList = list
                saveList(stationList)
            }
            return null
        }

        private fun saveList(stationList: List<Station>?) {
            if (stationList != null) {
                for (station in stationList) {
                    Log.i("station added", station.name ?: "null station name")
                    StationDatabase.getRoomDB(mWeakRef.get()!!).stationDao().save(station)
                }
            }
        }
    }

    fun initDayNightTheme() {
        //if no preference is saved or Logged Out, app will default to SYSTEM
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        var savedMode = ThemeUtil.getSavedThemePreference(getApplication())
        if (currentMode != savedMode) {
            CrashLogUtil.log("Theme mode initialized: $savedMode")
            if(savedMode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
                savedMode = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                else
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(savedMode)
        }
    }

    companion object {

        /*
        TODO: update this count whenever a new station is built
        grab entire list first time and save count to shared prefs.
        check shared prefs for cou
     */
        private const val BART_STATIONS_COUNT = 49 //berryessa is the latest
    }
}

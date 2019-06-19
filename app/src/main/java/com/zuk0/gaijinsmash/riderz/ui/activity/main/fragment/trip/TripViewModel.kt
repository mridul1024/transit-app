package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.zuk0.gaijinsmash.riderz.R

import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils

import javax.inject.Singleton

@Singleton
class TripViewModel : ViewModel() {

    //STATE
    lateinit var stationsList: Array<String>
    lateinit var originField: String
    lateinit var destinationField: String
    lateinit var dateField: String
    lateinit var timeField: String

    private fun initBundle(origin: String, destination: String, date: String, time: String): Bundle {
        val bundle = Bundle()
        bundle.putString(ORIGIN_KEY, origin)
        bundle.putString(DESTINATION_KEY, destination)
        bundle.putString(DATE_KEY, date)
        bundle.putString(TIME_KEY, time)
        return bundle
    }

    fun saveState() {

    }

    fun restoreState() {

    }

    fun doesStationExist(station: String, context: Context?): Boolean {
        context?.let {
            val stations = context.resources.getStringArray(R.array.stations_list)

            //todo: optimize search
            for (x in stations) {
                if (station == x) {
                    return true
                }
            }
        }
        return false
    }


    fun getTimeForTripSearch(preformatTime: String, is24HrTimeOn: Boolean): String {
        val result: String
        if (preformatTime != "Now" && is24HrTimeOn) {
            val convertedTime = TimeDateUtils.convertTo12HrForTrip(preformatTime)
            result = TimeDateUtils.formatTime(convertedTime)
        } else if (preformatTime != "Now" && !is24HrTimeOn) {
            result = TimeDateUtils.formatTime(preformatTime) // time=h:mm+AM/PM
        } else {
            result = preformatTime
        }
        return result
    }

    companion object {
        const val ORIGIN_KEY = "ORIGIN_KEY"
        const val DESTINATION_KEY = "DESTINATION_KEY"
        const val DATE_KEY = "DATE_KEY"
        const val TIME_KEY = "TIME_KEY"
        const val TRAIN_HEADERS_KEY = "TRAIN_HEADERS_KEY"
    }
}

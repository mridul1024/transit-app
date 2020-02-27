package com.zuk0.gaijinsmash.riderz.utils

import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils
import java.util.*

object StationUtils {

    fun getStationNameFromAbbr(abbr: String): String? {
        return StationList.stationMap[abbr.toUpperCase(Locale.US)] // keys are case sensitive
    }

    fun getAbbrFromStationName(name: String?): String? {
        var result = ""
        for ((key, value) in StationList.stationMap) {
            if (value.equals(name, ignoreCase = true)) {
                result = key
            }
        }
        return result
    }

    fun validateStationName(name: String?): Boolean {
        return StationList.stationMap.containsValue(name)
    }
}
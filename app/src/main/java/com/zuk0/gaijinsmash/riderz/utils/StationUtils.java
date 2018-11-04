package com.zuk0.gaijinsmash.riderz.utils;

import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList;

import java.util.Locale;
import java.util.Map;

public class StationUtils {

    public static String getStationNameFromAbbr(String abbr) {
        return StationList.stationMap.get(abbr.toLowerCase(Locale.US)); // keys are case sensitive
    }

    public static String getAbbrFromStationName(String name) {
        String result = "";
        for(Map.Entry<String, String> entry : StationList.stationMap.entrySet()){
            if(entry.getValue().equalsIgnoreCase(name)) {
               result = entry.getKey();
            }
        }
        return result;
    }
}

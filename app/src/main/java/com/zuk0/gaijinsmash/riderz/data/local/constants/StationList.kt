package com.zuk0.gaijinsmash.riderz.data.local.constants

import java.util.*

object StationList {
    //private ArrayMap<String, String> stationMap;
    var stationMap = HashMap<String, String>()

    fun getAbbrFromStationName(name: String?): String {
        var abbr = ""
        for ((key, value) in stationMap) {
            if (value.equals(name, ignoreCase = true)) {
                abbr = key
            }
        }
        return abbr
    }

    init {
        stationMap["12TH"] = "12th St. Oakland City Center"
        stationMap["16TH"] = "16th St. Mission"
        stationMap["19TH"] = "19th St. Oakland"
        stationMap["24TH"] = "24th St. Mission"
        stationMap["ASHB"] = "Ashby"
        stationMap["ANTC"] = "Antioch"
        stationMap["BALB"] = "Balboa Park"
        stationMap["BAYF"] = "Bay Fair"
        stationMap["CAST"] = "Castro Valley"
        stationMap["CIVC"] = "Civic Center"
        stationMap["COLS"] = "Coliseum"
        stationMap["COLM"] = "Colma"
        stationMap["CONC"] = "Concord"
        stationMap["DALY"] = "Daly City"
        stationMap["DBRK"] = "Downtown Berkeley"
        stationMap["DUBL"] = "Dublin/Pleasanton"
        stationMap["DELN"] = "El Cerrito del Norte"
        stationMap["PLZA"] = "El Cerrito Plaza"
        stationMap["EMBR"] = "Embarcadero"
        stationMap["FRMT"] = "Fremont"
        stationMap["FTVL"] = "Fruitvale"
        stationMap["GLEN"] = "Glen Park"
        stationMap["HAYW"] = "Hayward"
        stationMap["LAFY"] = "Lafayette"
        stationMap["LAKE"] = "Lake Merrit"
        stationMap["MCAR"] = "MacArthur"
        stationMap["MLBR"] = "Millbrae"
        stationMap["MONT"] = "Montgomery St."
        stationMap["NBRK"] = "North Berkeley"
        stationMap["NCON"] = "North Concord/Martinez"
        stationMap["OAKL"] = "Oakland Int'l Airport"
        stationMap["ORIN"] = "Orinda"
        stationMap["PITT"] = "Pittsburg/Bay Point"
        stationMap["PCTR"] = "Pittsburg Center"
        stationMap["PHIL"] = "Pleasant Hill"
        stationMap["POWL"] = "Powell St."
        stationMap["RICH"] = "Richmond"
        stationMap["ROCK"] = "Rockridge"
        stationMap["SBRN"] = "San Bruno"
        stationMap["SFIA"] = "San Francisco International Airport"
        stationMap["SANL"] = "San Leandro"
        stationMap["SHAY"] = "South Hayward"
        stationMap["SSAN"] = "South San Francisco"
        stationMap["UCTY"] = "Union City"
        stationMap["WARM"] = "Warm Springs/South Fremont"
        stationMap["WCRK"] = "Walnut Creek"
        stationMap["WDUB"] = "West Dublin"
        stationMap["WOAK"] = "West Oakland"
    }
}
package com.zuk0.gaijinsmash.riderz.data.local.constants;

import java.util.HashMap;
import java.util.Map;

public class StationList {

    //private ArrayMap<String, String> stationMap;

    public static HashMap<String, String> stationMap = new HashMap<>();

    static {
        stationMap.put("12TH", "12th St. Oakland City Center");
        stationMap.put("16TH", "16th St. Mission");
        stationMap.put("19TH", "19th St. Oakland");
        stationMap.put("24TH", "24th St. Mission");
        stationMap.put("ASHB", "Ashby");
        stationMap.put("ANTC", "Antioch");
        stationMap.put("BALB", "Balboa Park");
        stationMap.put("BAYF", "Bay Fair");
        stationMap.put("CAST", "Castro Valley");
        stationMap.put("CIVC", "Civic Center");
        stationMap.put("COLS", "Coliseum");
        stationMap.put("COLM", "Colma");
        stationMap.put("CONC", "Concord");
        stationMap.put("DALY", "Daly City");
        stationMap.put("DBRK", "Downtown Berkeley");
        stationMap.put("DUBL", "Dublin/Pleasanton");
        stationMap.put("DELN", "El Cerrito del Norte");
        stationMap.put("PLZA", "El Cerrito Plaza");
        stationMap.put("EMBR", "Embarcadero");
        stationMap.put("FRMT", "Fremont");
        stationMap.put("FTVL", "Fruitvale");
        stationMap.put("GLEN", "Glen Park");
        stationMap.put("HAYW", "Hayward");
        stationMap.put("LAFY", "Lafayette");
        stationMap.put("LAKE", "Lake Merrit");
        stationMap.put("MCAR", "MacArthur");
        stationMap.put("MLBR", "Millbrae");
        stationMap.put("MONT", "Montgomery St.");
        stationMap.put("NBRK", "North Berkeley");
        stationMap.put("NCON", "North Concord/Martinez");
        stationMap.put("OAKL", "Oakland Int'l Airport");
        stationMap.put("ORIN", "Orinda");
        stationMap.put("PITT", "Pittsburg/Bay Point");
        stationMap.put("PCTR", "Pittsburg Center");
        stationMap.put("PHIL", "Pleasant Hill");
        stationMap.put("POWL", "Powell St.");
        stationMap.put("RICH", "Richmond");
        stationMap.put("ROCK", "Rockridge");
        stationMap.put("SBRN", "San Bruno");
        stationMap.put("SFIA", "San Francisco International Airport");
        stationMap.put("SANL", "San Leandro");
        stationMap.put("SHAY", "South Hayward");
        stationMap.put("SSAN", "South San Francisco");
        stationMap.put("UCTY", "Union City");
        stationMap.put("WARM", "Warm Springs/South Fremont");
        stationMap.put("WCRK", "Walnut Creek");
        stationMap.put("WDUB", "West Dublin");
        stationMap.put("WOAK", "West Oakland");
    }

    public static String getAbbrFromStationName(String name) {
        String abbr = "";
        for(Map.Entry<String, String> e : StationList.stationMap.entrySet()) {
            if(e.getValue().equalsIgnoreCase(name)) {
                abbr = e.getKey();
            }
        }
        return abbr;
    }

}

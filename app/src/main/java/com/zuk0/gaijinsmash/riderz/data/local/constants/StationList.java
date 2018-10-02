package com.zuk0.gaijinsmash.riderz.data.local.constants;

import android.util.ArrayMap;

import java.util.HashMap;
import java.util.Map;

public class StationList {

    //private ArrayMap<String, String> stationMap;

    public static HashMap<String, String> stationMap = new HashMap<>();

    static {
        stationMap.put("12th", "12th St. Oakland City Center");
        stationMap.put("16th", "16th St. Mission");
        stationMap.put("19th", "19th St. Oakland");
        stationMap.put("24th", "24th St. Mission");
        stationMap.put("ashb", "Ashby");
        stationMap.put("antc", "Antioch");
        stationMap.put("balb", "Balboa Park");
        stationMap.put("bayf", "Bay Fair");
        stationMap.put("cast", "Castro Valley");
        stationMap.put("civc", "Civic Center");
        stationMap.put("cols", "Coliseum");
        stationMap.put("colm", "Colma");
        stationMap.put("conc", "Concord");
        stationMap.put("daly", "Daly City");
        stationMap.put("dbrk", "Downtown Berkeley");
        stationMap.put("dubl", "Dublin/Pleasanton");
        stationMap.put("deln", "El Cerrito del Norte");
        stationMap.put("plza", "El Cerrito Plaza");
        stationMap.put("embr", "Embarcadero");
        stationMap.put("frmt", "Fremont");
        stationMap.put("ftvl", "Fruitvale");
        stationMap.put("glen", "Glen Park");
        stationMap.put("hayw", "Hayward");
        stationMap.put("lafy", "Lafayette");
        stationMap.put("lake", "Lake Merrit");
        stationMap.put("mcar", "MacArthur");
        stationMap.put("mlbr", "Millbrae");
        stationMap.put("mont", "Montgomery St.");
        stationMap.put("nbrk", "North Berkeley");
        stationMap.put("ncon", "North Concord/Martinez");
        stationMap.put("oakl", "Oakland Int'l Airport");
        stationMap.put("orin", "Orinda");
        stationMap.put("pitt", "Pittsburg/Bay Point");
        stationMap.put("pctr", "Pittsburg Center");
        stationMap.put("phil", "Pleasant Hill");
        stationMap.put("powl", "Powell St.");
        stationMap.put("rich", "Richmond");
        stationMap.put("rock", "Rockridge");
        stationMap.put("sbrn", "San Bruno");
        stationMap.put("sfia", "San Francisco Int'l Airport");
        stationMap.put("sanl", "San Leandro");
        stationMap.put("shay", "South Hayward");
        stationMap.put("ssan", "South San Francisco");
        stationMap.put("ucty", "Union City");
        stationMap.put("warm", "Warm Springs/South Fremont");
        stationMap.put("wcrk", "Walnut Creek");
        stationMap.put("wdub", "West Dublin");
        stationMap.put("woak", "West Oakland");
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

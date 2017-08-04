package com.example.gaijinsmash.transitapp.internet;

/**
 * Created by ryanj on 7/19/2017.
 */

public class ApiStringBuilder {
    private static final boolean DEBUG = true; // True: turns on debug logging, False: off
    private static final String API_KEY = "&key=Q7Z9-PZ53-9QXT-DWE9";
    private static final String BASE_URI = "http://api.bart.gov/api/";
    private static final String TEST_URI = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";

    //TODO : finish API calls

    public String getAllStations() {
        String stations = "stn.aspx?cmd=stns";
        return BASE_URI + stations + API_KEY;
    }

    public String getRoute(String stationA, String stationB) {
        // get abbr for stationA
        // get abbr for stationB
        String origin = null;
        String dest = null;
        return BASE_URI + "sched.aspx?cmd=depart&orig=" + origin + "&dest=" + dest + API_KEY;
     }

     public String getFare(String stationA, String stationB) {
        return BASE_URI;
     }

     public String getStationInfo(String station) {
        return BASE_URI;
     }

     public String getHolidayInfo(){
        return BASE_URI;
     }

     public String getRouteInfo() {
         return BASE_URI;
     }

     public String getBSA() {
         return BASE_URI;
     }
}

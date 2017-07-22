package com.example.gaijinsmash.transitapp.internet;

/**
 * Created by ryanj on 7/19/2017.
 */

public class ApiBuilder {
    private static final boolean DEBUG = true; // True: turns on debug logging, False: off
    private static final String API_KEY = "&key=Q7Z9-PZ53-9QXT-DWE9";
    private static final String BASE_URI = "http://api.bart.gov/api/";
    private static final String TEST_URI = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";

    private static final String ALL_STATIONS = "stn.aspx?cmd=stns";



    //TODO : finish API calls

    public String getStations() {
        return BASE_URI + ALL_STATIONS + API_KEY;
    }

    public String getRoute(String stationA, String stationB) {
        return BASE_URI + API_KEY;
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

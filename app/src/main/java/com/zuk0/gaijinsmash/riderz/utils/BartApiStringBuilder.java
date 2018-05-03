package com.zuk0.gaijinsmash.riderz.utils;

import android.util.Log;

public class BartApiStringBuilder {
    private static final String API_KEY = "&key=Q7Z9-PZ53-9QXT-DWE9";
    private static final String BASE_URI = "http://api.bart.gov/api/";
    private static final boolean DEBUG = true;

    public static String getAllStations() {
        String stations = "stn.aspx?cmd=stns";
        return BASE_URI + stations + API_KEY;
    }

    public static String getStationInfo(String abbreviatedStation) {
        String cmd = "stn.aspx?cmd=stninfo&orig=";
        return BASE_URI + cmd + abbreviatedStation + API_KEY;
    }

    /*
        parking_flag: 0 = No BART parking, 1 = Yes, parking available.
        bike_flag: 0 = No bike racks, 1 = Yes, bike racks available.
        bike_station_flag: 0 = Not a bike station, 1 = Yes, station is a bike station.
        locker_flag: 0 = No lockers, 1 = Yes, station has lockers.
    */

    public static String getStationAccess(String abbreviatedStation) {
        String cmd = "stn.aspx?cmd=stnaccess&orig=";
        return BASE_URI + cmd + abbreviatedStation + API_KEY;
    }

    public static String getRoute(String stationA, String stationB) {
        return BASE_URI + "sched.aspx?cmd=depart&orig=" + stationA + "&dest=" + stationB + API_KEY;
    }

    public static String getSpecialSchedule() {
        String cmd = "sched.aspx?cmd=special";
        return BASE_URI + cmd + API_KEY;
    }

    public static String getHolidayInfo(){
        String cmd = "sched.aspx?cmd=holiday";
        return BASE_URI + cmd + API_KEY;
    }

    /*
        stations are in capital Abbr
        date=<mm/dd/yyyy>
        time=h:mm+am/pm or now
        b=<number> how many trips to show before specified time(0,4)
        a=<number> how many trips to show after specified time (0,4)
        format is mm/dd/yyyy, now, or today.
    */
    public static String getDetailedRoute(String origin, String arrival, String date, String time) {
         String cmd = "sched.aspx?cmd=depart&orig=" + origin + "&dest=" + arrival + "&date=" + date + "&time=" + time + "&b=0&a=4";
         if(DEBUG) {
             Log.i("getDetailedRoute", BASE_URI + cmd + API_KEY);
         }
         return BASE_URI + cmd + API_KEY;
    }

    public static String getBSA() {
        String cmd = "bsa.aspx?cmd=bsa";
        return BASE_URI + cmd + API_KEY;
    }

    // valid directions are 'n' for Northbound and 's' for Southbound
    public static String getRealTimeEstimate(String origin, String direction) {
        String cmd = "etd.aspx?cmd=etd&orig=" + origin + "&dir=" + direction;
        return BASE_URI + cmd + API_KEY;
    }
}

package com.zuk0.gaijinsmash.riderz.utils;

public class HaversineFormulaUtils {

    private final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public static int calculateDistanceInKilometer(double userLat, double userLng,
                                            double stationLat, double stationLong) {

        double latDistance = Math.toRadians(userLat - stationLat);
        double lngDistance = Math.toRadians(userLng - stationLong);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(stationLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }
}

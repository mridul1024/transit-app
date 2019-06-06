package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import android.location.Location
import android.util.Log

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import java.util.Objects

/*
    This class provides a formula to calculate distance
    between two locations on Lat/Lng coordinates.
    Compliments of stackoverflow!
    TODO: write unit tests for this class.
 */
object HaversineFormulaUtils {

    private val AVERAGE_RADIUS_OF_EARTH_KM = 6371.0


    /*
        Brute Force Method of finding closest station.
        FIXME: Need to create a Graph for all stations and use BFS.
     */
    internal fun findNearestStation(context: Context, destinationStation: String,
                                    list: List<Station>): Station? {
        // get user location
        val gps = GpsUtils(context)
        val userLocation = gps.location

        // keep track of closest station
        var closestStation: Station? = null
        var closestDistance = 0

        if (userLocation != null) {
            // else, calculate distance to closest station
            for (station in list) {
                val stationLatitude = station.latitude
                val stationLongitude = station.longitude

                val distanceBetween = calculateDistanceInKilometer(userLocation.latitude, userLocation.longitude,
                        stationLatitude, stationLongitude)
                if (closestDistance == 0) {
                    closestDistance = distanceBetween
                    closestStation = station
                } else {
                    if (closestDistance > distanceBetween) {
                        closestDistance = distanceBetween
                        closestStation = station
                        Log.i("closestStation", station.name)
                    }
                }
            }
            // if user is already near destination station, alert user
            if (destinationStation.equals(Objects.requireNonNull<Station>(closestStation).name, ignoreCase = true)) {
                Log.i("closestStation", "USER IS ALREADY THERE")
                return null
            }
        } else {
            Log.i("userLoc", "NULL")
            return null
        }
        return closestStation
    }

    fun calculateDistanceInKilometer(userLat: Double, userLng: Double,
                                     stationLat: Double, stationLong: Double): Int {

        val latDistance = Math.toRadians(userLat - stationLat)
        val lngDistance = Math.toRadians(userLng - stationLong)

        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(stationLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2))

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return Math.round(HaversineFormulaUtils.AVERAGE_RADIUS_OF_EARTH_KM * c).toInt()
    }
}

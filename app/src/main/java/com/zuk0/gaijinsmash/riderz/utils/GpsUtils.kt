package com.zuk0.gaijinsmash.riderz.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver

import android.util.Log
import com.orhanobut.logger.Logger

/*
    TODO: add lifecycle handling
 */
class GpsUtils(context: Context) : LocationListener, LifecycleObserver {

    private val TAG = "GpsUtils"
    val location: Location?
    private var mLatitude: Double = 0.toDouble()
    private var mLongitude: Double = 0.toDouble()
    val locationProvider: String

    init {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE // Define the criteria for how to select the location provider

        locationProvider = initLocationProvider(context, manager, criteria)
        location = initLocation(manager, context)

        // Initialize the location fields
        location?.let { onLocationChanged(it) } ?: Log.i("GPS:", "Location is not available")
    }

    private fun initLocationProvider(context: Context, manager: LocationManager?, criteria: Criteria): String {
        var locationProvider = ""
        if (checkLocationPermission(context)) {
            if (manager != null) {
                locationProvider = manager.getBestProvider(criteria, true)
                Log.i(TAG, locationProvider)
            }
        }
        return locationProvider
    }

    private fun initLocation(manager: LocationManager, context: Context): Location? {
        var location: Location? = null
        if (checkLocationPermission(context)) {
            location = manager.getLastKnownLocation(locationProvider)
            if (location == null) {
                location = tryAllProviders(manager, context)
            }
        }
        return location
    }

    private fun tryAllProviders(manager: LocationManager, context: Context): Location? {
        var location: Location? = null
        if (checkLocationPermission(context)) {
            for (provider in manager.allProviders) {
                location = manager.getLastKnownLocation(provider)
                return location ?: continue
            }
        }
        return location
    }

    override fun onLocationChanged(location: Location) {
        mLatitude = this.location?.latitude ?: 0.0
        mLongitude = this.location?.longitude ?: 0.0
    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
        Logger.d(s)
    }

    override fun onProviderEnabled(s: String) {
        Logger.d(s)
    }

    override fun onProviderDisabled(s: String) {
        Logger.d(s)
    }

    companion object {

        fun checkLocationPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }
} // End of Class

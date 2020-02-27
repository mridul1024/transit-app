package com.zuk0.gaijinsmash.riderz.data.local.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver

import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.orhanobut.logger.Logger

class LocationManager(context: Context) : LocationListener, LifecycleObserver {

    var location: Location?

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()
    private val locationProvider: String

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        //todo
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        //cache location
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        //get cache from bundle?
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        location = null
    }

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
                locationProvider = manager.getBestProvider(criteria, true) as String
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
        this.location = location
        latitude = location.latitude ?: 0.0
        longitude = location.longitude ?: 0.0
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
        private const val TAG = "LocationManager"

        const val LOCATION_PERMISSION_REQUEST_CODE = 121
        fun checkLocationPermission(context: Context?): Boolean {
            if(context == null)
                return false
            return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
        fun checkIfExplanationIsNeeded(activity: Activity?) : Boolean {
            if(activity == null)
                return false

            return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
} // End of Class

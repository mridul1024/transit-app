package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import com.zuk0.gaijinsmash.riderz.utils.HaversineFormulaUtils

import java.util.Calendar
import java.util.Locale
import java.util.Objects
import java.util.TimeZone

import javax.inject.Inject
import javax.inject.Singleton

class GoogleMapViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    var stationList: List<Station>? = null //todo put in viewmodel
    private var mStationsLivedata: LiveData<List<Station>>? = null

    val stationsLiveData: LiveData<List<Station>>?
        get() {
            if (mStationsLivedata == null) {
                mStationsLivedata = StationDatabase.getRoomDB(getApplication())?.stationDao()?.stationsLiveData
            }
            return mStationsLivedata
        }

    // For Google Map
    fun initMapSettings(map: GoogleMap) {
        Log.i("initMapSettings", "googlemap")
        // Set boundary of map area
        val bayArea = LatLngBounds(
                SOUTH_WEST_BOUNDS,
                NORTH_EAST_BOUNDS)
        map.setLatLngBoundsForCameraTarget(bayArea)
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
        map.setMinZoomPreference(10f)
    }

    fun initBartMap(context:
                    Context?, imageView: ImageView?) {
        if(context != null && imageView != null) {
            val img: Drawable?
            val cal = Calendar.getInstance(TimeZone.getTimeZone("PST"), Locale.US)
            val day = cal.get(Calendar.DAY_OF_WEEK)
            if (day == 7) {
                img = context.getDrawable(R.drawable.bart_map_sunday)
            } else {
                img = context.getDrawable(R.drawable.bart_map_weekday_sat)
            }
            Glide.with(context)
                    .load(img)
                    .into(imageView)
        }
    }

    //todo: refactor as this is inside  another class now.
    fun findNearestMarker(map: GoogleMap, destinationCoordinates: LatLng, destinationStation: String, list: List<Station>?): Station? {
        if(list != null) {
            // get user location
            val gps = LocationManager(getApplication())
            val userLocation = gps.location

            // keep track of closest station
            var closestStation: Station? = null
            var closestDistance = 0

            if (userLocation != null) {
                // else, calculate distance to closest station
                for (station in list) {
                    val stationLatitude = station.latitude
                    val stationLongitude = station.longitude

                    val distanceBetween = HaversineFormulaUtils.calculateDistanceInKilometer(userLocation.latitude, userLocation.longitude,
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
        return null
    }

    fun initLocationFromBundle(bundle: Bundle?, googleMap: GoogleMap) {
        if (bundle != null) {
            val stationTitle = bundle.getString("StationTitle")
            val latLng = LatLng(java.lang.Double.valueOf(Objects.requireNonNull<String>(bundle.getString("StationLat"))), java.lang.Double.valueOf(Objects.requireNonNull<String>(bundle.getString("StationLong"))))
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(12f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            val marker = googleMap.addMarker(MarkerOptions().position(latLng).title(stationTitle))
            marker.showInfoWindow()
        }
    }

    fun initDefaultLocation(map: GoogleMap) {
        val defaultLocation = DEFAULT_LOCATION
        val DEFAULT_ZOOM = 6f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM))
    }

    companion object {

        // Map Constraints
        private val SOUTH_WEST_BOUNDS = LatLng(37.2982, -121.5363)
        private val NORTH_EAST_BOUNDS = LatLng(38.0694, -121.7438)
        private val DEFAULT_LOCATION = LatLng(37.73659478, -122.19683306)
    }
}

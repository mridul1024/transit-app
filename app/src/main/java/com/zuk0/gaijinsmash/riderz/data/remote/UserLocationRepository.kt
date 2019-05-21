package com.zuk0.gaijinsmash.riderz.data.remote

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocationRepository
@Inject constructor(context: Context,
                    val gpsUtils: GpsUtils){

    fun getUserLocation() : MutableLiveData<Location> { // lat/long?
        val location = MutableLiveData<Location>()
        val userLocation = gpsUtils.location
        location.postValue(userLocation)
        return location
    }

    fun save() {
        //todo implement with user settings...? shared preferences?
    }

    fun getCache() {
        //if user sharedpreferences has users location settings. use that. else. use GpsUtils.
    }
}
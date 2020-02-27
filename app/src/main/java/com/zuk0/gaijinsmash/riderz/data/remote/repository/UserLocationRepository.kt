package com.zuk0.gaijinsmash.riderz.data.remote.repository

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocationRepository
@Inject constructor(context: Context,
                    val locationUtils: LocationManager){

    fun getUserLocation() : MutableLiveData<Location> { // lat/long?
        val location = MutableLiveData<Location>()
        val userLocation = locationUtils.location
        location.postValue(userLocation)
        return location
    }

    fun save() {
        //todo implement with user settings...? shared preferences?
    }

    fun getCache() {
        //if user sharedpreferences has users location settings. use that. else. use LocationManager.
    }
}
package com.zuk0.gaijinsmash.riderz.data.remote.repository

import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository
@Inject constructor(val locationUtils: LocationManager) {

    fun getUserLocation() {

    }

}
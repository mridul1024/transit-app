package com.zuk0.gaijinsmash.riderz.data.remote.repository

import com.zuk0.gaijinsmash.riderz.utils.GpsUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository
@Inject constructor(val gpsUtils: GpsUtils) {

    fun getUserLocation() {

    }

}
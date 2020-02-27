package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse
import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository
import javax.inject.Inject

class StationInfoViewModel
@Inject constructor(val mRepository: StationRepository) : ViewModel() {

    private var mStationLiveData: LiveData<StationXmlResponse>? = null
    var mStationAbbr: String? = null
    var mStationObject: Station? = null
    var mBundle: Bundle? = null

    @Synchronized
    private fun initData() { //todo: initialization logic here
    }

    fun getStationLiveData(abbr: String): LiveData<StationXmlResponse>? {
        if (mStationLiveData == null) {
            mStationLiveData = mRepository.getStation(abbr)
        }
        return mStationLiveData
    }

    internal enum class StationInfo(val value: String) {
        Name("StationName"), Lat("StationLat"), Long("StationLong");

    }

    fun getBundle(station: Station): Bundle {
        val bundle = Bundle()
        bundle.putString(StationInfo.Name.value, station.name)
        bundle.putString(StationInfo.Lat.value, java.lang.String.valueOf(station.latitude))
        bundle.putString(StationInfo.Long.value, java.lang.String.valueOf(station.longitude))
        return bundle
    }

    init {
        initData()
    }
}
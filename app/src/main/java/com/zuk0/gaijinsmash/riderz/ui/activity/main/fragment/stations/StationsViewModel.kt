package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository
import javax.inject.Inject

class StationsViewModel
@Inject constructor(val mRepository: StationRepository) : ViewModel() {

    private var mListFromRepo: LiveData<StationXmlResponse>? = null

    fun getListFromDb(context: Context?): LiveData<List<Station>>? {
        return StationDatabase.getRoomDB(context!!)?.stationDao()?.stationsLiveData
    }

    val listFromRepo: LiveData<StationXmlResponse>
        get() {
            if (mListFromRepo == null) {
                mListFromRepo = mRepository.stations
            }
            return mListFromRepo!!
        }

}
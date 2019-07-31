package com.zuk0.gaijinsmash.riderz.utils.graph

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.room.Room
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase

class StationMatrix(context: Context) : LifecycleObserver {

    var adjacencyList = mutableListOf<StationNode>()

    //grab all stations
    var stations: List<Station>
    var db: StationDatabase = StationDatabase.getRoomDB(context)

    init {
        stations = db.stationDAO.allStations
    }

    fun createList(station: Station) : MutableList<StationNode> {
        val list = mutableListOf<StationNode>()
        return list
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        db.close()
    }
}
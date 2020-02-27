package com.zuk0.gaijinsmash.riderz.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationRepository
@Inject constructor(val service: BartService, val stationDAO: StationDao, val executor: Executor) {
    fun getStation(abbr: String): LiveData<StationXmlResponse> { //todo: if cached != null, return cached
        val data = MutableLiveData<StationXmlResponse>()
        service?.getStation(abbr)?.enqueue(object : Callback<StationXmlResponse> {
            override fun onResponse(call: Call<StationXmlResponse>, response: Response<StationXmlResponse>) {
                data.postValue(response.body())
                Log.i("Station", response.message())
            }

            override fun onFailure(call: Call<StationXmlResponse>, t: Throwable) {
                Log.wtf("onFailure", "station: " + t.message)
            }
        })
        return data
    }

    val stations: LiveData<StationXmlResponse>
        get() {
            val data = MutableLiveData<StationXmlResponse>()

            service?.getAllStations()?.enqueue(object : Callback<StationXmlResponse> {
                override fun onResponse(call: Call<StationXmlResponse>, response: Response<StationXmlResponse>) {
                    data.postValue(response.body())
                    Log.i("Stations", response.message())
                }

                override fun onFailure(call: Call<StationXmlResponse>, t: Throwable) {
                    Log.wtf("StationRepository", t.message)
                }
            })
            return data
        }

    fun refreshStation(name: String?) {
      /*  executor.execute {
            //todo: check if station exists, then check if station data exists
            val stationExists = stationDAO.getStationByName(name) != null
            val stationData = stationDAO.getStationByName(name)!!.intro
            val stationDataExists = stationData != ""
            if (!stationExists) { // refresh data
                try {
                    val response: Response<*> = service.getStation(name).execute()
                    stationDAO.save(response.body() as Station?)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }*/
    }

}
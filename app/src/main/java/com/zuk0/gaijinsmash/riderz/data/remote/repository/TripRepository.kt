package com.zuk0.gaijinsmash.riderz.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.orhanobut.logger.Logger

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper

import java.util.concurrent.Executor

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Singleton
class TripRepository
@Inject constructor(val service: BartService, val tripDao: TripDao, val executor: Executor) {

    fun getTrip(origin: String?, destination: String?, date: String?, time: String?): LiveData<LiveDataWrapper<TripJsonResponse>> {
        val data = MutableLiveData<LiveDataWrapper<TripJsonResponse>>()

        service.getTripJson(origin, destination, date, time).enqueue(object : Callback<TripJsonResponse> {

            override fun onResponse(call: Call<TripJsonResponse>, response: Response<TripJsonResponse>) {
                Logger.d("Response Success: ${response.body()}")
                val res = LiveDataWrapper.success(response.body()) as LiveDataWrapper<TripJsonResponse>
                data.postValue(res)
            }

            override fun onFailure(call: Call<TripJsonResponse>, t: Throwable) {
                Logger.e("onFailure: ${t.message}")
                t.printStackTrace()
                val res = LiveDataWrapper.error(null, t.message) as LiveDataWrapper<TripJsonResponse>
                data.postValue(res)
            }
        })
        return data
    }
}

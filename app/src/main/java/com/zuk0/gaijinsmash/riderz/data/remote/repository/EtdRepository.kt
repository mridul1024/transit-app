package com.zuk0.gaijinsmash.riderz.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper.Companion.error
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper.Companion.success
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EtdRepository
@Inject constructor(val service: BartService, val etdDao: EtdDao, val executor: Executor) {
    fun getEtd(originAbbr: String?): LiveData<LiveDataWrapper<EtdXmlResponse>> { //todo: if cached != null, return cached
        val data = MutableLiveData<LiveDataWrapper<EtdXmlResponse>>()
        service.getEtd(originAbbr).enqueue(object : Callback<EtdXmlResponse> {
            override fun onResponse(call: Call<EtdXmlResponse>, response: Response<EtdXmlResponse>) {
                Logger.i(response.message())
                val res = success(response.body()) as LiveDataWrapper<EtdXmlResponse>
                data.postValue(res)
            }

            override fun onFailure(call: Call<EtdXmlResponse>, t: Throwable) {
                Log.wtf("EtdRepository", t.message)
                val res = error<Any?>(null, t.localizedMessage) as LiveDataWrapper<EtdXmlResponse>
                data.postValue(res)
            }
        })
        return data
    }

    fun refreshEtd(origin: String?) { //todo: insert logic
    }

}
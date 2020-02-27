package com.zuk0.gaijinsmash.riderz.data.remote.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.BuildConfig
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService
import com.zuk0.gaijinsmash.riderz.utils.MockBartApiUtil.getMockBsaResponse
import com.zuk0.gaijinsmash.riderz.utils.xml_parser.BsaXmlParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.sql.Timestamp
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

/*
    This repo abstracts the data sources from the rest of the app. ViewModel doesn't know
    that the data is fetched b  y the BartService. Repository makes API calls and fetches data.
    https://proandroiddev.com/the-missing-google-sample-of-android-architecture-components-guide-c7d6e7306b8f
*/
@Singleton
class BsaRepository // for caching
// for executing on new thread
@Inject constructor(val service: BartService, val bsaDao: BsaDao, val executor: Executor) {

    fun getBsaJson(context: Context) : LiveData<BsaJsonResponse> {
        val data = MutableLiveData<BsaJsonResponse>()
        service.getBsaJson().enqueue(object: Callback<BsaJsonResponse> {
            override fun onFailure(call: Call<BsaJsonResponse>, t: Throwable) {
               t.message?.let{ Logger.e(it) }
            }

            override fun onResponse(call: Call<BsaJsonResponse>, response: Response<BsaJsonResponse>) {
                data.postValue(response.body())
            }
        })
        return data
    }

    fun getBsa(context: Context): LiveData<BsaXmlResponse> { //refreshBsa(new Timestamp(System.currentTimeMillis()));
        val data = MutableLiveData<BsaXmlResponse>()
        if (!BuildConfig.DEBUG) { //todo
            val `is` = getMockBsaResponse(context)
            if (`is` != null) {
                try {
                    val list = BsaXmlParser(`is`).list
                    val res = BsaXmlResponse()
                    res.date = "Today"
                    res.bsaList = list
                    data.postValue(res)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        } else {

            service.getBsa().enqueue(object : Callback<BsaXmlResponse> {
                override fun onResponse(call: Call<BsaXmlResponse>, response: Response<BsaXmlResponse>) {
                    data.postValue(response.body())
                }

                override fun onFailure(call: Call<BsaXmlResponse>, t: Throwable) {
                    Log.e("onFailure", "bsa: " + t.message)
                }
            })
        }
        return data
    }

    private fun refreshBsa(current: Timestamp) { // running in background thread
    /*    executor.execute {
            // No more than 5 minutes in the past
            val min = current.time - 60000 * 5
            val bsaExists = bsaDao.bsaExists(Timestamp(min), current)
            if (!bsaExists) {
                try {
                    val response: Response<BsaXmlResponse> = service.getBsa().execute()
                    val bsa = response.body() as BsaXmlResponse?
                    if (bsa != null) {
                        bsa.timestamp = Timestamp(System.currentTimeMillis())
                    }
                    bsaDao.save(bsa)
                } catch (e: IOException) {
                    Log.wtf("refreshBsa()", e.message)
                }
            }
        }*/
    }

}
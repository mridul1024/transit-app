package com.zuk0.gaijinsmash.riderz.data.remote.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.BuildConfig
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService
import com.zuk0.gaijinsmash.riderz.utils.CrashLogUtil
import com.zuk0.gaijinsmash.riderz.utils.MockBartApiUtil.getMockBsaResponse
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import com.zuk0.gaijinsmash.riderz.utils.xml_parser.BsaXmlParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.sql.Date
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

    fun getBsa(): LiveData<BsaXmlResponse> {
        val data = MutableLiveData<BsaXmlResponse>()
        executor.execute {
            var isCacheExpired = true
            val cachedBsa: BsaXmlResponse? = bsaDao.getLatest()
            if(cachedBsa != null) {
                if(cachedBsa.timestamp != null) {
                    val cachedDate = Date(cachedBsa.timestamp!!.time)
                    val result = TimeDateUtils.durationOfMinutesBetweenDates(cachedDate, Date(System.currentTimeMillis()))
                    if(result < MAX_MINUTES) {
                        // use cache
                        isCacheExpired = false
                        data.postValue(cachedBsa)
                        CrashLogUtil.log("Using cached BSA")
                    }
                }
            }
            //request from server
            if(isCacheExpired) {
                CrashLogUtil.log("Cache expired - fetching remotely")
                try {
                    val response: Response<BsaXmlResponse> = service.getBsa().execute()
                    val bsa = response.body() as BsaXmlResponse?
                    if (bsa != null) {
                        bsa.timestamp = Timestamp(System.currentTimeMillis())
                    }
                    bsaDao.save(bsa)
                    data.postValue(bsa)
                } catch (e: IOException) {
                    Log.wtf("refreshBsa()", e.message)
                }
            }
        }
        return data
    }

    private fun getCached(context: Context?) : LiveData<BsaXmlResponse?> { // running in background thread

        //refreshBsa(new Timestamp(System.currentTimeMillis()));

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

            //TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
            service.getBsa().enqueue(object : Callback<BsaXmlResponse> {
                override fun onResponse(call: Call<BsaXmlResponse>, response: Response<BsaXmlResponse>) {
                    response.body()?.let {
                        it.timestamp = Timestamp(System.currentTimeMillis())
                        bsaDao.save(it)
                    }
                    data.postValue(response.body())
                }

                override fun onFailure(call: Call<BsaXmlResponse>, t: Throwable) {
                    Log.e("onFailure", "bsa: " + t.message)
                }
            })
        }
        return data
    }

    companion object {
        private const val TAG = "BsaRepository"
        private const val MAX_MINUTES = 5
    }

}
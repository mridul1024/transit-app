package com.zuk0.gaijinsmash.riderz.ui.fragment.home

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.lang.ref.WeakReference

import javax.inject.Inject
import javax.inject.Singleton

/*
    ViewModel classes stores UI data and is lifecycle aware and can handle orientation changes.
    AndroidViewModel is for classes that need to use Context.
 */
@Singleton
class HomeViewModel @Inject
internal constructor(private val application: Application,
                     private val mTripRepository: TripRepository,
                     private val mBsaRepository: BsaRepository,
                     private val mEtdRepository: EtdRepository) : ViewModel() {

    val bsaLiveData: LiveData<BsaXmlResponse>
        get() {
            return mBsaRepository.bsa
        }

    // For the home screen banner picture
    val hour: Int
        get() = TimeDateUtils.getCurrentHour()

    // Create message for Advisory Time and Date
    fun is24HrTimeOn(context: Context): Boolean {
        return SharedPreferencesUtils.getTimePreference(context)
    }

    private fun initTime(is24HrTimeOn: Boolean, time: String): String {
        val result: String
        if (is24HrTimeOn) {
            result = TimeDateUtils.format24hrTime(time)
        } else {
            result = TimeDateUtils.convertTo12Hr(time)
        }
        return result
    }

    fun initMessage(context: Context, is24HrTimeOn: Boolean, time: String): String {
        return context.resources.getString(R.string.last_update) + " " + initTime(is24HrTimeOn, time)
    }

    fun initPic(context: Context, hour: Int, imageView: ImageView) {
        if (hour < 6 || hour >= 21) {
            // show night picture
            Glide.with(context)
                    .load(R.drawable.sf_night)
                    .into(imageView)
        } else if (hour >= 17) {
            // show dusk picture
            Glide.with(context)
                    .load(R.drawable.sf_dusk)
                    .into(imageView)
        } else {
            Glide.with(context)
                    .load(R.drawable.sf_day)
                    .into(imageView)
        }
    }

    fun doesPriorityExist() : Boolean {
        val db = Room.databaseBuilder(application, FavoriteDatabase::class.java,
                "favorites").allowMainThreadQueries().build()
        val count = db.favoriteDAO.priorityCount
        db.close()
        if(count > 0) {
            return true
        }
        return false
    }

    fun getFavorite() : Favorite {
        val db = Room.databaseBuilder(application, FavoriteDatabase::class.java,
                "favorites").allowMainThreadQueries().build()
        val favorite = db.favoriteDAO.priorityFavorite
        db.close()
        return favorite
    }

    fun getEtdLiveData(origin: String): LiveData<EtdXmlResponse>? {
        val originAbbr = getAbbr(origin)
        return mEtdRepository.getEtd(originAbbr)
    }

    fun getAbbr(stationName : String) : String {
        val db = Room.databaseBuilder(application, StationDatabase::class.java,
                "stations").allowMainThreadQueries().build()
        val abbr = db.stationDAO.getStationByName(stationName).abbr
        db.close()
        return abbr
    }

    fun getTripLiveData(origin: String, destination: String) : LiveData<TripJsonResponse>? {
        return mTripRepository.getTrip(getAbbr(origin), getAbbr(destination), "TODAY", "NOW")
    }

} // end of class

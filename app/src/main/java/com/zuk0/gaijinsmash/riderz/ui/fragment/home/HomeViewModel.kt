package com.zuk0.gaijinsmash.riderz.ui.fragment.home

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.Room
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository
import com.zuk0.gaijinsmash.riderz.ui.fragment.ParcelableViewModel
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils
import com.zuk0.gaijinsmash.riderz.utils.StationUtils
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import java.util.*
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
                     private val mEtdRepository: EtdRepository) : ViewModel(), ParcelableViewModel {

    var mBundle : Bundle = Bundle()
    var mBsaList : List<Bsa> = ArrayList()

    override fun writeTo(bundle: Bundle) {
        mBundle = bundle
    }

    override fun readFrom(bundle: Bundle) {

    }

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
        val originAbbr = StationUtils.getAbbrFromStationName(origin)
        return mEtdRepository.getEtd(originAbbr)
    }

    fun createFavoriteInverse(trips: List<Trip>, favorite: Favorite) : Favorite {
        val trainHeaders = ArrayList<String>()
        for (trip in trips) {
            val header = trip.getLegList().get(0).getTrainHeadStation()
            if (!trainHeaders.contains(header)) {
                trainHeaders.add(header) // add a unique train header
                Log.i("HEADER", header)
            }
        }
        val favoriteInverse = Favorite()
        favoriteInverse.origin = favorite.getDestination()
        favoriteInverse.destination = favorite.getOrigin()
        favoriteInverse.trainHeaderStations = trainHeaders
        return favoriteInverse
    }

    fun getTripLiveData(origin: String, destination: String) : LiveData<TripJsonResponse>? {
        return mTripRepository.getTrip(StationUtils.getAbbrFromStationName(origin),
                StationUtils.getAbbrFromStationName(destination), "TODAY", "NOW")
    }

    fun getEstimatesFromEtd(favorite: Favorite, etdList: List<Etd>) : List<Estimate> {
        var results = ArrayList<Estimate>()
        for(etd in etdList) {
            if(favorite.trainHeaderStations.contains(etd.destinationAbbr)) {
                val estimate = etd.estimateList.get(0)
                estimate.origin = favorite.origin
                estimate.destination = favorite.destination
                results.add(estimate)
            }
        }
        return results
    }

    //todo: get current state of each timer
    //todo: if all timers are
    fun getRemainingTime() {

    }

    companion object {
        fun beginTimer(textView: TextView, minutesLeft: Int) {

            val untilFinished = (minutesLeft * 60000).toLong()
            object : CountDownTimer(untilFinished, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val minutes = textView.context.resources.getString(R.string.minutes)
                    val seconds = textView.context.resources.getString(R.string.seconds)
                    textView.text = (millisUntilFinished / 60000).toString() + " " + minutes + " : " + millisUntilFinished % 60000 / 1000 + " " + seconds
                }

                override fun onFinish() {
                    textView.text = "Leaving!"
                    // todo: if leaving, wait 1 minute, then destroy itemListRow.
                    // must have reference to adapter.
                }
            }.start()
        }
    }

} // end of class

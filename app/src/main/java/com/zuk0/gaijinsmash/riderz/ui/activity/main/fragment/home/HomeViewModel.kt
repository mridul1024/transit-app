package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.Manifest
import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import io.reactivex.Maybe

import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log

import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.WeatherRepository
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.utils.*

import java.util.ArrayList
import java.util.Objects

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeViewModel @Inject
constructor(private val mApplication: Application, //FIXME - use androidviewmodel
            private val mTripRepository: TripRepository,
            private val mBsaRepository: BsaRepository,
            private val mEtdRepository: EtdRepository,
            private val mWeatherRepository: WeatherRepository) : ViewModel() {

    private var db: FavoriteDatabase? = null

    // STATE
    var isFavoriteAvailable = false
    var closestStation: Station? = null
    var userLocation: Location? = null
    lateinit var gpsUtils: GpsUtils //todo use dagger

    val bsaLiveData: LiveData<BsaXmlResponse>
        get() = mBsaRepository.bsa

    //todo db calls should be abstracted to a repository class
    val maybeFavorite: Maybe<Favorite>
        get() = db!!.favoriteDAO.priorityFavorite

    /*
        TODO grab user input from the CREATE view and automatically refresh the homepage.
     */

    internal//todo - abstract to repository
    //if you have a commute route, get geoloc of destination
    //else use user's current location
    val userLocationLiveData: LiveData<Location>
        get() {
            val userLocationLiveData = MutableLiveData<Location>()
            val userLocation: Location
            val gps = GpsUtils(mApplication)
            userLocation = gps.location
            userLocationLiveData.postValue(userLocation)
            return userLocationLiveData
        }

    internal val isDaytime: Boolean
        get() = TimeDateUtils.isDaytime()

    init {

        initDb() // todo
    }

    private fun initDb() {
        db = Room.databaseBuilder(Objects.requireNonNull(mApplication.applicationContext), FavoriteDatabase::class.java,
                "favorites").build()
    }

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

    internal fun initMessage(context: Context, is24HrTimeOn: Boolean, time: String): String {
        return context.resources.getString(R.string.last_update) + " " + initTime(is24HrTimeOn, time)
    }

    internal fun getEtdLiveData(origin: String): LiveData<EtdXmlResponse> {
        val originAbbr = StationUtils.getAbbrFromStationName(origin)
        return mEtdRepository.getEtd(originAbbr)
    }

    fun setTrainHeaders(trips: List<Trip>, favorite: Favorite) {
        val trainHeaders = ArrayList<String>()
        for (trip in trips) {
            val header = StationUtils.getAbbrFromStationName(trip.legList[0].trainHeadStation).toUpperCase()
            if (!trainHeaders.contains(header)) {
                trainHeaders.add(header)
                Log.i("HEADER", header)
            }
        }
        favorite.trainHeaderStations = trainHeaders //todo: use hashset
    }

    fun createFavoriteInverse(trips: List<Trip>, favorite: Favorite): Favorite {
        val favoriteInverse = Favorite()
        favoriteInverse.origin = favorite.destination
        favoriteInverse.destination = favorite.origin
        setTrainHeaders(trips, favoriteInverse)
        return favoriteInverse
    }

    internal fun getTripLiveData(origin: String, destination: String): LiveData<LiveDataWrapper<TripJsonResponse>> {
        return mTripRepository.getTrip(StationUtils.getAbbrFromStationName(origin),
                StationUtils.getAbbrFromStationName(destination), "TODAY", "NOW")
    }

    /*
        For comparisons - make sure all train headers are abbreviated and capitalized
     */
    internal fun getEstimatesFromEtd(favorite: Favorite, etds: List<Etd>): List<Estimate> {
        val results = ArrayList<Estimate>()
        for (etd in etds) {
            if (favorite.trainHeaderStations.contains(etd.destinationAbbr.toUpperCase())) {
                val estimate = etd.estimateList[0]
                estimate.origin = favorite.origin
                estimate.destination = favorite.destination
                estimate.trainHeaderStation = etd.destination
                results.add(estimate)
            }
        }
        return results
    }

    //F = 9/5 (K - 273) + 32
    internal fun kelvinToFahrenheit(temp: Double): Double {
        return 9f / 5f * (temp - 273) + 32
    }

    //C = K - 273
    internal fun kelvinToCelcius(temp: Double): Double {
        return temp - 273
    }

    internal fun checkHolidaySchedule() {
        //TODO: push news to home fragment if it's a holiday
    }

    internal fun getUserLocation(context: Activity) {
        try {
            if (GpsUtils.checkLocationPermission(context)) {
                gpsUtils = GpsUtils(context)
                userLocation = gpsUtils.location
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSON_REQUEST_CODE)
                } else {
                    // TODO handle request for api < 23
                }
            }
        } catch (e: SecurityException) {
            Logger.e(e.localizedMessage)
        }

    }

    //get user location, use haversine formula to get nearest station.
    internal fun getNearestStation(userLat: Double, userLong: Double,
                                   stationLat: Double, stationLong: Double) : Station? {

        if(closestStation != null)
            return closestStation

        gpsUtils = GpsUtils(mApplication)
        userLocation = gpsUtils.location

        var closestDistance = 0

        //need station list.
        val list = getStations()
        if(userLocation != null) {
            for (station in list) {
                val stationLat = station.latitude
                val stationLong = station.longitude

                val distanceBetween = HaversineFormulaUtils.calculateDistanceInKilometer(
                        userLocation!!.latitude,
                        userLocation!!.longitude,
                        stationLat,
                        stationLong)

                if(closestDistance == 0) {
                    closestDistance = distanceBetween
                    closestStation = station
                } else {
                    if(closestDistance > distanceBetween) {
                        closestDistance = distanceBetween
                        closestStation = station
                    }
                }
            }
            //TODO use closest station to fetch  general directions for upcoming trains
            Logger.i("closest station: ${closestStation?.name}")
            //TODO if user has a commute/favorite route
        }
        return closestStation
    }

    private fun getStations() : List<Station> { //todo
        return StationDatabase.getRoomDB(mApplication).stationDAO.allStations
    }

    internal fun getWeather(zipcode: Int): LiveData<LiveDataWrapper<WeatherResponse>> {
        return mWeatherRepository.getWeather(zipcode)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared()")
        db?.close()
    }

    companion object {
        private val TAG = "HomeViewModel"
        private const val LOCATION_PERMISSON_REQUEST_CODE = 101
    }
}

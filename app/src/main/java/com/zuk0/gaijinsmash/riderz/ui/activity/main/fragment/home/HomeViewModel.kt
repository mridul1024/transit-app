package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.room.Room
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.BuildConfig
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
import io.reactivex.Maybe
import kotlinx.coroutines.*
import java.util.*
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
    val userLocation: Location? by lazy {
        val gps = GpsUtils(mApplication)
        gps.location
    }

    // List
    var mInverseEstimateList: List<Estimate>? = null //todo refactor - put in viewmodel
    var mFavoriteEstimateList: List<Estimate>? = null //todo refactor - put in viewmodel
    var upcomingNearbyEstimateList: List<Estimate>? = null

    private val defaultStation = Station()
    private val closestStationLiveData = MutableLiveData<Station>()

    val bsaLiveData: LiveData<BsaXmlResponse>
        get() = mBsaRepository.bsa

    //todo db calls should be abstracted to a repository class
    val maybeFavorite: Maybe<Favorite>
        get() = db!!.favoriteDAO.priorityFavorite

    /*
        TODO grab user input from the CREATE view and automatically refresh the homepage.
     */
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

    internal fun getEtdLiveData(origin: String): LiveData<LiveDataWrapper<EtdXmlResponse>> {
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
        favoriteInverse.a = favorite.b
        favoriteInverse.b = favorite.a
        setTrainHeaders(trips, favoriteInverse)
        return favoriteInverse
    }

    internal fun getTripLiveData(origin: String, destination: String): LiveData<LiveDataWrapper<TripJsonResponse>> {
        return mTripRepository.getTrip(StationUtils.getAbbrFromStationName(origin),
                StationUtils.getAbbrFromStationName(destination), "TODAY", "NOW")
    }

    private val mediatorLiveData = MediatorLiveData<LiveDataWrapper<EtdXmlResponse>>()

    fun getLocalEtdMediator(): MediatorLiveData<LiveDataWrapper<EtdXmlResponse>> {
        val source = getNearestStation(userLocation)
        mediatorLiveData.removeSource(source)
        mediatorLiveData.addSource(source) { station ->
            Transformations.map(mEtdRepository.getEtd(station.abbr)) { mediatorLiveData.postValue(it) }
        }
        return mediatorLiveData
    }

    fun getLocalEtd(station: Station) : LiveData<LiveDataWrapper<EtdXmlResponse>> {
        return mEtdRepository.getEtd(station.abbr)
    }
    /*
        For comparisons - make sure all train headers are abbreviated and capitalized
        For Setting estimates- use the full name of station
     */
    internal fun getEstimatesFromEtd(favorite: Favorite, etds: List<Etd>): List<Estimate> {
        val results = ArrayList<Estimate>()
        for (etd in etds) {
            if (favorite.trainHeaderStations?.contains(etd.destinationAbbr.toUpperCase()) == true) {
                val estimate = etd.estimateList[0]
                estimate.origin = favorite.a?.name
                estimate.destination = favorite.b?.name
                estimate.trainHeaderStation = etd.destination
                results.add(estimate)
            }
        }
        return results
    }

    /**
     * get a list of Estimate objects
     */
    fun getEstimatesFromEtd(station: Station)  : List<Estimate>  {
        val origin = station.name
        val etds = station.etdList
        val results = ArrayList<Estimate>()
        etds?.let {
            for (etd in etds) {
                val estimate = etd.estimateList[0]
                estimate.origin = origin
                estimate.destination = etd.destination
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
        //https://www.bart.gov/guide/holidays
        //xmas, nye,
    }


    //get user location, use haversine formula to get nearest station.
    fun getNearestStation(userLocation: Location?) : LiveData<Station> {

        if(userLocation == null) {
            return closestStationLiveData
        }

        viewModelScope.launch(Dispatchers.IO) {
            var closestDistance = 0
            val list  =  getStationsFromDb()
            for (station in list) {
                val stationLat = station.latitude
                val stationLong = station.longitude

                val distanceBetween = HaversineFormulaUtils.calculateDistanceInKilometer(
                        userLocation.latitude,
                        userLocation.longitude,
                        stationLat,
                        stationLong)

                if(closestDistance == 0) {
                    closestDistance = distanceBetween
                    closestStation = station
                } else if(closestDistance > distanceBetween) {
                    closestDistance = distanceBetween
                    closestStation = station
                }
            }
            Logger.i("closest station: ${closestStation?.name}")
            closestStationLiveData.postValue(closestStation)
        }

        return closestStationLiveData
    }

    private fun getStationsFromDb() : List<Station> {
       return StationDatabase.getRoomDB(mApplication).stationDAO.allStations
    }

    internal fun getWeather(): LiveData<LiveDataWrapper<WeatherResponse>> {
        userLocation?.let {
            return mWeatherRepository.getWeatherByGeoloc(it.latitude, it.longitude)
        }
        return mWeatherRepository.getWeatherByZipcode(94108) //default if userLocation is unavailable
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

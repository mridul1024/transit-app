package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.utils.*
import io.reactivex.Maybe
import kotlinx.coroutines.*
import java.util.*
import java.util.Observer
import javax.inject.Inject
import javax.inject.Singleton

class HomeViewModel @Inject
constructor(application: Application,
            private val mTripRepository: TripRepository,
            private val mBsaRepository: BsaRepository,
            private val mEtdRepository: EtdRepository) : AndroidViewModel(application) {

    private var db: FavoriteDatabase? = null
    private var stationDB: StationDatabase? = null

    // Fragment STATE
    var isFavoriteAvailable = false
    var closestStation: Station? = null

    // EtdPresenter State
    var selectedStation: Station? = null
    var upcomingNearbyEstimateList: MutableList<Estimate>? = null
    var mInverseEstimateList: MutableList<Estimate>? = null
    var mFavoriteEstimateList: MutableList<Estimate>? = null

    private var userLocation: Location? = null
    var isLocationPermissionEnabled = false

    fun getLocation() : Location? {
        if(userLocation == null) {
            val gps = LocationManager(getApplication())
            return gps.location
        }
        return userLocation
    }

    // Communication Bridges
    val isLocationPermissionEnabledLD = MutableLiveData<Boolean>()
    private val closestStationLiveData = MutableLiveData<Station>()

    val bsaLiveData: LiveData<BsaXmlResponse>
        get() = mBsaRepository.getBsa(getApplication())

    //todo db calls should be abstracted to a repository class
    val maybeFavorite: Maybe<Favorite>
        get() = db?.favoriteDAO()?.priorityFavorite as Maybe<Favorite>

    /*
        TODO grab user input from the CREATE view and automatically refresh the homepage.
     */
    init {
        initDb() // todo
    }

    fun getBsaJson() : LiveData<BsaJsonResponse> {
        return mBsaRepository.getBsaJson(getApplication())
    }
    private fun initDb() {
        db = Room.databaseBuilder(getApplication(), FavoriteDatabase::class.java,
                "favorites").build()
        stationDB = Room.databaseBuilder(getApplication(), StationDatabase::class.java, "stations").build()
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

    fun getEtdLiveData(origin: String): LiveData<LiveDataWrapper<EtdXmlResponse>> {
        val originAbbr = StationUtils.getAbbrFromStationName(origin)
        return mEtdRepository.getEtd(originAbbr)
    }

    fun setTrainHeaders(trips: List<Trip>, favorite: Favorite) {
        val trainHeaders = ArrayList<String>()
        for (trip in trips) {
            val header = StationUtils.getAbbrFromStationName(trip.legList?.get(0)?.trainHeadStation)?.toUpperCase()
            if (!trainHeaders.contains(header) && header != null) {
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

    /*
    map is useful when you want to make changes to the value before dispatching it to the UI.
    switchMap is useful when you want to return different LiveData based upon the value of the first one.
     */

    //get station, then post value of
    fun getEstimatesLiveData(station: Station): LiveData<LiveDataWrapper<EtdXmlResponse>> {
        return mEtdRepository.getEtd(station.abbr)
    }

    /*
        For comparisons - make sure all train headers are abbreviated and capitalized
        For Setting estimates- use the full name of station
     */
    internal fun getEstimatesFromEtd(favorite: Favorite, etds: List<Etd>): MutableList<Estimate> {
        val results = ArrayList<Estimate>()
        for (etd in etds) {
            if (favorite.trainHeaderStations?.contains(etd.destinationAbbr?.toUpperCase()) == true) {
                val estimate = etd.estimateList?.get(0)
                estimate?.let{
                    estimate.origin = favorite.a?.name
                    estimate.destination = favorite.b?.name
                    estimate.trainHeaderStation = etd.destination
                    results.add(estimate)
                }
            }
        }
        return results
    }

    /**
     * get a list of Estimate objects
     */
    fun getEstimatesFromEtd(station: Station) : MutableList<Estimate>  {
        val origin = station.name
        val etds = station.etdList
        val results = ArrayList<Estimate>()
        etds?.let {
            for (etd in etds) {
                val estimate = etd.estimateList?.get(0)
                estimate?.let {
                    estimate.origin = origin
                    estimate.destination = etd.destination
                    results.add(estimate)
                }
            }
        }
        return results
    }

    internal fun checkHolidaySchedule() {
        //TODO: push news to home fragment if it's a holiday
        //https://www.bart.gov/guide/holidays
        //xmas, nye,
    }

    private val mediatorLiveData = MediatorLiveData<LiveDataWrapper<EtdXmlResponse>>()

    private val calculateDistances = FloatArray(2)

    /**
     * important!
     */
    private fun calculateDistanceBetween(endLat: Double, endLong: Double, startLat: Double, startLong: Double) {
        Location.distanceBetween(endLat, endLong, startLat, startLong, calculateDistances)
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

    fun getStationsFromDb() : List<Station> {
       return StationDatabase.getRoomDB(getApplication())?.stationDao()?.allStations ?: emptyList()
    }

    fun getFavoritesList(favorite: Favorite) {
        //todo update
    }

    val platform1 = mutableListOf<Etd>()
    val platform2 = mutableListOf<Etd>()


    //for each destination has its own destination
    fun createEstimateListsByPlatform(list: MutableList<Etd>?) {
        list?.let {
            platform1.clear()
            platform2.clear()

            for(i in list) {
                if(i.estimateList?.get(0)?.platform == 1) {
                    platform1.add(i)
                } else {
                    platform2.add(i)
                }
            }
        }
    }

    fun getPlatformTitle(list: List<Etd>) : String {
        if(list.isNotEmpty())
            return list[0].estimateList?.get(0)?.platform.toString()
        return ""
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared()")
        db?.close()
        stationDB?.close()
    }

    companion object {
        private val TAG = "HomeViewModel"
    }
}

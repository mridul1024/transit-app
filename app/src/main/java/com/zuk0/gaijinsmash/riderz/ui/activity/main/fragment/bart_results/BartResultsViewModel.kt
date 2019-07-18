package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.*

import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.constants.RiderzEnums
import com.zuk0.gaijinsmash.riderz.data.local.entity.results.BaseResult
import com.zuk0.gaijinsmash.riderz.data.local.entity.results.TripDataResult
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.simpleframework.xml.transform.Transform

import java.lang.ref.WeakReference
import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class BartResultsViewModel @Inject
internal constructor(application: Application, private val mTripRepository: TripRepository) : AndroidViewModel(application) , CoroutineScope{

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()
    var origin: String = ""
    var destination: String = ""
    var date: String = "today"
    var time: String = "now"
    var isFromRecyclerAdapter = false

    var mFavoriteObject: Favorite? = null
    var mTripList: List<Trip>? = null

    private var stationsMediator = MediatorLiveData<LiveDataWrapper<TripJsonResponse>>()
    private var tripMediator = MediatorLiveData<LiveDataWrapper<TripJsonResponse>>()
    private lateinit var tripLiveData: LiveData<LiveDataWrapper<TripJsonResponse>>

    var favorite: Favorite? = null
        private set

    fun handleIntentExtras(arguments: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            origin = bundle.getString(TripFragment.TripBundle.ORIGIN.value) ?: ""
            destination = bundle.getString(TripFragment.TripBundle.DESTINATION.value) ?: ""
            date = bundle.getString(TripFragment.TripBundle.DATE.value) as String
            time = bundle.getString(TripFragment.TripBundle.TIME.value) as String
            isFromRecyclerAdapter = bundle.getBoolean("FAVORITE_RECYCLER_ADAPTER") // todo create const
        }
    }

    fun saveState(outState: Bundle) {
        //todo
    }

    fun restoreState(inState: Bundle?) {
        //todo

    }
    /****************************************************************
     * Trips - origin and destination must be in abbreviated form
     */
    internal fun getTrip(origin: String, destination: String, date: String, time: String): LiveData<LiveDataWrapper<TripJsonResponse>>? {
        tripLiveData = mTripRepository.getTrip(origin, destination, date, time)
        return tripLiveData
    }

    internal fun getStationsFromDb(origin: String?, destination: String?): LiveData<List<Station>> {
        return StationDatabase.getRoomDB(getApplication()).stationDAO.getOriginAndDestination(origin, destination)
    }

    /*
        Note: parameters must be formatted properly before submission
        Check bart api docs for reference.
     */

    fun loadTrip2(origin: String, destination: String, date: String, time: String) : MediatorLiveData<LiveDataWrapper<TripJsonResponse>> {
        var originResult: Station = Station()
        var destinationResult: Station = Station()

        stationsMediator.addSource(getStationByName(origin)) { value ->
            originResult = value
            getTrip(originResult.abbr, destinationResult.abbr, date, time)
        }

        stationsMediator.addSource(getStationByName(destination)) { value ->
            destinationResult = value
            getTrip(originResult.abbr, destinationResult.abbr, date, time)
        }

        stationsMediator.addSource(tripLiveData) {
            stationsMediator.postValue(it)
        }

        return stationsMediator
    }

    fun loadTrip(origin: String?, destination: String?, date: String, time: String): LiveData<LiveDataWrapper<TripJsonResponse>> {
        val result = TripDataResult()

        if(origin.isNullOrBlank() || destination.isNullOrBlank()) {
            result.status = BaseResult.Status.ERROR
        } else {
            stationsMediator.addSource(getStationByName(origin)) {
                value -> result.origin = value.abbr
            }
            stationsMediator.addSource(getStationByName(destination)) {
                value -> result.destination = value.abbr
            }
        }
        return tripMediator
    }

    private fun getStationByName(name: String) : LiveData<Station> {
        return StationDatabase.getRoomDB(getApplication()).stationDAO.getStationLiveDataByName(name)
    }

    /****************************************************************
     * Favorites
     */
    internal fun createFavorite(origin: String?, destination: String?, tripList: List<Trip>?): Favorite? {
        if (origin != null && destination != null && tripList != null) {
            favorite = Favorite()
            favorite?.origin = origin
            favorite?.destination = destination
            val trainHeaders = ArrayList<String>()
            for (trip in tripList) {
                val header = trip.legList[0].trainHeadStation
                if (!trainHeaders.contains(header)) {
                    trainHeaders.add(header) // add a unique train header
                    Log.i("HEADER", header)
                }
            }
            favorite?.trainHeaderStations = trainHeaders
        }
        return favorite
    }

    internal fun handleFavoritesIcon(action: RiderzEnums.FavoritesAction, favorite: Favorite) {
        AddOrRemoveFavoriteTask(getApplication(), action, favorite).execute()
    }

    internal fun getFavoriteLiveData(origin: String, destination: String): LiveData<Favorite> {
        return FavoriteDatabase.getRoomDB(getApplication()).favoriteDAO.getLiveDataFavorite(origin, destination)
    }


    private suspend fun handleFavoriteTask() = withContext(Dispatchers.IO) {

    }

    //todo use coroutines here
    inner class AddOrRemoveFavoriteTask(context: Application, private val mAction: RiderzEnums.FavoritesAction, private val mFavorite: Favorite) : AsyncTask<Void, Void, Void>() {

        private val mWeakRef: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg voids: Void): Void? {
            when (mAction) {
                RiderzEnums.FavoritesAction.ADD_FAVORITE -> {
                    if (FavoriteDatabase.getRoomDB(mWeakRef.get()).favoriteDAO.priorityCount == 0) {
                        mFavorite.priority = Favorite.Priority.ON
                    } else {
                        mFavorite.priority = Favorite.Priority.OFF
                    }
                    FavoriteDatabase.getRoomDB(mWeakRef.get()).favoriteDAO.save(mFavorite)
                }
                RiderzEnums.FavoritesAction.DELETE_FAVORITE -> {
                    val dao = FavoriteDatabase.getRoomDB(mWeakRef.get()).favoriteDAO
                    val one = dao.getFavorite(mFavorite.origin, mFavorite.destination)
                    val two = dao.getFavorite(mFavorite.destination, mFavorite.origin)
                    if (one != null) {
                        dao.delete(one)
                    }
                    if (two != null) {
                        dao.delete(two)
                    }
                }
            }
            return null
        }
    }

    override fun onCleared() {
        super.onCleared()
        //todo clear jobs
    }

    companion object {
        const val KEY_TRIP_RESULTS = ""
        const val KEY_TRIP_LAYOUT_MANAGER = ""
    }
}

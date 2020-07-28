package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.*
import com.orhanobut.logger.Logger

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
import com.zuk0.gaijinsmash.riderz.utils.StationUtils
import kotlinx.coroutines.*
import org.simpleframework.xml.transform.Transform

import java.lang.ref.WeakReference
import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class BartResultsViewModel @Inject
internal constructor(application: Application, private val mTripRepository: TripRepository) : AndroidViewModel(application){

    var originStation: Station? = null
    var destinationStation: Station? = null
    var origin: String = ""
    var destination: String = ""
    var date: String = "today" // default
    var time: String = "now" // default
    var isFromRecyclerAdapter = false

    var mFavoriteObject: Favorite? = null
    var mTripList: List<Trip>? = null
    var favorite: Favorite? = null
        private set

    fun handleIntentExtras(arguments: Bundle?) {
        if (arguments != null) {
            origin = arguments.getString(TripFragment.TripBundle.ORIGIN.value) ?: ""
            destination = arguments.getString(TripFragment.TripBundle.DESTINATION.value) ?: ""
            date = arguments.getString(TripFragment.TripBundle.DATE.value) as String
            time = arguments.getString(TripFragment.TripBundle.TIME.value) as String
            isFromRecyclerAdapter = arguments.getBoolean("FAVORITE_RECYCLER_ADAPTER") // todo create const
        }
    }

    private fun getStationAbbr(stationName: String) : String? {
        return StationUtils.getAbbrFromStationName(stationName)
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
    /*
        Note: parameters must be formatted properly before submission
        Check bart api docs for reference.
     */
    private val mediator = MediatorLiveData<LiveDataWrapper<TripJsonResponse>>()

    fun loadTrip(origin: String, destination: String, date: String, time: String): LiveData<LiveDataWrapper<TripJsonResponse>>  {

        viewModelScope.launch {
            val originTask = async(Dispatchers.IO) { getStationFromDb(origin) }
            val destinationTask = async(Dispatchers.IO)  { getStationFromDb(destination) }
            originStation = originTask.await()
            destinationStation = destinationTask.await()

            if(originStation?.abbr?.isNotBlank() == true && destinationStation?.abbr?.isNotBlank() == true) {
                Logger.d("Origin:  ${originStation?.name}, Destination: ${destinationStation?.name}")
                mediator.addSource(mTripRepository.getTrip(originStation?.abbr as String, destinationStation?.abbr as String, date, time)) { result ->
                    mediator.postValue(result)
                }
            }
        }
        return mediator
    }

    /**
     * Fetches a station by name from the db
     */
    internal fun getStationFromDb(name:  String): Station? {
        return StationDatabase.getRoomDB(getApplication())?.stationDao()?.getStationByName(name)
    }
    /****************************************************************
     * Favorites
     */
    internal fun createFavorite(origin: Station?, destination: Station?, tripList: List<Trip>?): Favorite? {
        if (origin != null && destination != null && tripList != null) {
            favorite = Favorite()
            favorite?.a = origin
            favorite?.b = destination
            val trainHeaders = ArrayList<String>()
            for (trip in tripList) {
                val header = trip.legList?.get(0)?.trainHeadStation
                if (!trainHeaders.contains(header)) {
                    trainHeaders.add(header!!) // add a unique train header
                    Log.i("HEADER", header)
                }
            }
            favorite?.trainHeaderStations = trainHeaders
        }
        return favorite
    }

    internal fun handleFavoritesIcon(action: RiderzEnums.FavoritesAction, favorite: Favorite) {
        addOrRemoveFavorite(action, favorite)
    }

    internal fun getFavoriteLiveData(a: Station, b: Station): LiveData<Favorite>? {
        return FavoriteDatabase.getRoomDB(getApplication())?.favoriteDAO()?.getLiveDataFavorite(a, b) //todo
    }

    private fun handleFavoriteTask(context: Context, action: RiderzEnums.FavoritesAction, favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = FavoriteDatabase.getRoomDB(context)
            when (action) {
                RiderzEnums.FavoritesAction.ADD_FAVORITE -> {
                    if (db?.favoriteDAO()?.priorityCount == 0) {
                        favorite.priority = Favorite.Priority.ON
                    } else {
                        favorite.priority = Favorite.Priority.OFF
                    }
                    db?.favoriteDAO()?.save(favorite)
                }
                RiderzEnums.FavoritesAction.DELETE_FAVORITE -> {
                    val dao = db?.favoriteDAO()
                    val one = dao?.getFavorite(favorite.a, favorite.b)
                    val two = dao?.getFavorite(favorite.b, favorite.a)
                    if(one == null && two == null) {
                        Logger.e("unable to locate favorite object in db: $favorite")
                    }
                    if (one != null) {
                        dao.delete(one)
                    }
                    if (two != null) {
                        dao.delete(two)
                    }
                }
                else ->  Logger.wtf("unhandled action: $action")
            }
        }
    }


    fun addOrRemoveFavorite(action: RiderzEnums.FavoritesAction, favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            when (action) {
                RiderzEnums.FavoritesAction.ADD_FAVORITE -> {
                    if (FavoriteDatabase.getRoomDB(getApplication())?.favoriteDAO()?.priorityCount == 0) {
                        favorite.priority = Favorite.Priority.ON
                    } else {
                        favorite.priority = Favorite.Priority.OFF
                    }
                    FavoriteDatabase.getRoomDB(getApplication())?.favoriteDAO()?.save(favorite)
                }
                RiderzEnums.FavoritesAction.DELETE_FAVORITE -> {
                    val dao = FavoriteDatabase.getRoomDB(getApplication())?.favoriteDAO()
                    val one = dao?.getFavorite(favorite.a, favorite.b)
                    val two = dao?.getFavorite(favorite.b, favorite.a)
                    if (one != null) {
                        dao.delete(one)
                    }
                    if (two != null) {
                        dao.delete(two)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.d("onCleared")
    }

    companion object {
        const val KEY_TRIP_RESULTS = ""
        const val KEY_TRIP_LAYOUT_MANAGER = ""
    }
}

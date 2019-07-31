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
    /*
        Note: parameters must be formatted properly before submission
        Check bart api docs for reference.
     */
    private val mediator = MediatorLiveData<LiveDataWrapper<TripJsonResponse>>()

    fun loadTrip(origin: String, destination: String, date: String, time: String): LiveData<LiveDataWrapper<TripJsonResponse>>  {
        viewModelScope.launch {
            val originTask = async(Dispatchers.IO) { getStationFromDb(origin) }
            val destinationTask = async(Dispatchers.IO)  { getStationFromDb(destination) }
            val result1 = originTask.await()
            val result2 = destinationTask.await()

            if(result1.abbr.isNotBlank() && result2.abbr.isNotBlank()) {
                Logger.d("Origin:  ${result1.name}, Destination: ${result2.name}")
                mediator.addSource(mTripRepository.getTrip(originTask.await().abbr, destinationTask.await().abbr, date, time)) { result ->
                    mediator.postValue(result)
                }
            }
        }
        return mediator
    }

    /**
     * Fetches a station by name from the db
     */
    internal fun getStationFromDb(name:  String): Station {
        return StationDatabase.getRoomDB(getApplication()).stationDAO.getStationByName(name);
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

    private fun handleFavoriteTask(context: Context, action: RiderzEnums.FavoritesAction, favorite: Favorite) {

        viewModelScope.launch(Dispatchers.IO) {
            val db = FavoriteDatabase.getRoomDB(context)
            when (action) {
                RiderzEnums.FavoritesAction.ADD_FAVORITE -> {
                    if (db.favoriteDAO.priorityCount == 0) {
                        favorite.priority = Favorite.Priority.ON
                    } else {
                        favorite.priority = Favorite.Priority.OFF
                    }
                    db.favoriteDAO.save(favorite)
                }
                RiderzEnums.FavoritesAction.DELETE_FAVORITE -> {
                    val dao = db.favoriteDAO
                    val one = dao.getFavorite(favorite.origin, favorite.destination)
                    val two = dao.getFavorite(favorite.destination, favorite.origin)
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
        Logger.d("onCleared")
    }

    companion object {
        const val KEY_TRIP_RESULTS = ""
        const val KEY_TRIP_LAYOUT_MANAGER = ""
    }
}

package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import androidx.lifecycle.viewModelScope

import com.zuk0.gaijinsmash.riderz.data.local.constants.RiderzEnums
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import java.lang.ref.WeakReference
import javax.inject.Inject

import javax.inject.Singleton

@Singleton
class FavoritesViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    val db = FavoriteDatabase.getRoomDB(getApplication())
    val stationsDb = StationDatabase.getRoomDB(getApplication())

    val favorites: LiveData<MutableList<Favorite>>?
        get() = db?.favoriteDAO()?.allFavoritesLiveData

    private enum class FavoriteAction {
        DeleteFavorite, SetAsPriority, DeletePriority
    }

    fun saveFavorite(depart: String, arrive: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fav = Favorite()
            val taskA = async {
                stationsDb?.stationDao()?.getStationByName(depart)
            }
            val taskB = async {
                stationsDb?.stationDao()?.getStationByName(arrive)
            }
            fav.a = taskA.await()
            fav.b = taskB.await()
            db?.favoriteDAO()?.save(fav)
        }
    }


    //todo: replace with rxjava
    private class FavoritesTask(context: Context, private val mAction: RiderzEnums.FavoritesAction, private val mFavorite: Favorite) : AsyncTask<Void, Void, Boolean>() {

        private val mWeakRef: WeakReference<Context>

        init {
            mWeakRef = WeakReference(context)
        }

        override fun doInBackground(vararg voids: Void): Boolean? {
            when (mAction) {
                RiderzEnums.FavoritesAction.ADD_PRIORITY -> {
                    if (FavoriteDatabase.getRoomDB(mWeakRef.get()!!)?.favoriteDAO()?.priorityCount!! > 0) {
                        return false
                    }
                    if (FavoriteDatabase.getRoomDB(mWeakRef.get()!!)?.favoriteDAO()?.priorityCount == 0) {
                        FavoriteDatabase.getRoomDB(mWeakRef.get()!!)?.favoriteDAO()?.updatePriorityById(mFavorite.id)
                        return true
                    }
                    return false
                }
                RiderzEnums.FavoritesAction.DELETE_PRIORITY -> {
                    FavoriteDatabase.getRoomDB(mWeakRef.get()!!)?.favoriteDAO()?.removePriorityById(mFavorite.id)
                    return true
                }
                RiderzEnums.FavoritesAction.DELETE_FAVORITE -> {
                    FavoriteDatabase.getRoomDB(mWeakRef.get()!!)?.favoriteDAO()?.delete(mFavorite)
                    return true
                }
            }
            return null
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            if (result!!) {
                when (mAction) {
                    RiderzEnums.FavoritesAction.ADD_PRIORITY -> Toast.makeText(mWeakRef.get(), "Set as Priority", Toast.LENGTH_SHORT).show()
                    RiderzEnums.FavoritesAction.DELETE_PRIORITY -> Toast.makeText(mWeakRef.get(), "Favorite is no longer Priority", Toast.LENGTH_SHORT).show()
                    RiderzEnums.FavoritesAction.DELETE_FAVORITE -> Toast.makeText(mWeakRef.get(), "Favorite Deleted", Toast.LENGTH_SHORT).show()
                }

            } else {
                when (mAction) {
                    RiderzEnums.FavoritesAction.ADD_PRIORITY -> Toast.makeText(mWeakRef.get(), "Only one favorite can be your priority", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        db?.close()
        stationsDb?.close()
    }

    companion object {

        fun deleteFavorite(context: Context, favorite: Favorite) {
            FavoritesTask(context, RiderzEnums.FavoritesAction.DELETE_FAVORITE, favorite).execute()
        }

        fun addPriority(context: Context, favorite: Favorite) {
            FavoritesTask(context, RiderzEnums.FavoritesAction.ADD_PRIORITY, favorite).execute()
        }

        fun removePriority(context: Context, favorite: Favorite) {
            FavoritesTask(context, RiderzEnums.FavoritesAction.DELETE_PRIORITY, favorite).execute()
        }
    }
}

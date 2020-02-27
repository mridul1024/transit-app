package com.zuk0.gaijinsmash.riderz.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import io.reactivex.Maybe

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(favorite: Favorite?)

    @Delete
    fun delete(favorite: Favorite?)

    @Query("SELECT * from favorites where id = :id")
    fun getFavoriteById(id: Int): Favorite?

    @get:Query("SELECT * from favorites")
    val allFavoritesLiveData: LiveData<MutableList<Favorite>>

    @get:Query("SELECT * from favorites")
    val list: MutableList<Favorite>?

    @Query("SELECT COUNT(*) from favorites")
    fun countFavorites(): Int

    @Query("SELECT * from favorites where a = :a and b = :b")
    fun getFavorite(a: Station?, b: Station?): Favorite?

    @get:Query("SELECT * from favorites where priority = 1")
    val priorityFavorite: Maybe<Favorite>

    //Update a priority favorite
    @Query("Update favorites set priority = 1 where id = :id")
    fun updatePriorityById(id: Int)

    //Update priority to null
    @Query("Update favorites set priority = 0 where id = :id")
    fun removePriorityById(id: Int)

    @Query("SELECT * from favorites where a = :a and b = :b OR a = :b and b = :a")
    fun getLiveDataFavorite(a: Station, b: Station): LiveData<Favorite>

    @get:Query("SELECT count(*) from favorites where priority = 1")
    val priorityCount: Int
}
package com.zuk0.gaijinsmash.riderz.data.local.room.dao

import androidx.room.*
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(trip: Trip?)

    @Delete
    fun delete(trip: Trip?)

    @Query("SELECT * from trips where origin = :origin and destination = :destination")
    fun load(origin: String?, destination: String?): Trip?

    @Query("SELECT * from trips where id = :id")
    fun getTripById(id: Int): Trip?
}
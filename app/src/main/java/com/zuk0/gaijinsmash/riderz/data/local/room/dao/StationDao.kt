package com.zuk0.gaijinsmash.riderz.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station

@Dao
interface StationDao {
    // Removes a station from the database
    @Delete
    fun delete(station: Station)

    @Query("SELECT * from stations where id = :id")
    fun getStationByID(id: Int): Station?

    // Gets all stations from the database
    @get:Query("SELECT * from stations")
    val allStations: List<Station>

    @get:Query("SELECT * from stations")
    val stationsLiveData: LiveData<List<Station>>

    // Select station by matching Longitude coordinate
    @Query("SELECT * from stations where longitude = :longitude")
    fun getStationByLongitude(longitude: String?): Station?

    @Query("SELECT * from stations where name = :origin or name = :destination")
    fun getOriginAndDestination(origin: String?, destination: String?): LiveData<List<Station>>

    @Query("SELECT * from stations where name = :name")
    fun getStationByName(name: String?): Station?

    @Query("SELECT * from stations where address = :address")
    fun getStationByAddress(address: String?): LiveData<Station>

    @Query("SELECT * from stations where name = :name")
    fun getStationLiveDataByName(name: String?): LiveData<Station>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateStation(station: Station?)

    @Query("SELECT COUNT(*) from stations")
    fun countStations(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(station: Station?)
}
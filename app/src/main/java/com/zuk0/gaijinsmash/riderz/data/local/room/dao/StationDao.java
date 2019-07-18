package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface StationDao {

    // Removes a station from the database
    @Delete
    void delete(Station station);

    @Query("SELECT * from stations where id = :id")
    Station getStationByID(int id);

    // Gets all stations from the database
    @Query("SELECT * from stations")
    LiveData<List<Station>> getAllStations();

    @Query("SELECT * from stations")
    LiveData<List<Station>> getStationsLiveData();

    // Select station by matching Longitude coordinate
    @Query("SELECT * from stations where longitude = :longitude")
    Station getStationByLongitude(String longitude);

    @Query("SELECT * from stations where name = :origin or name = :destination")
    LiveData<List<Station>> getOriginAndDestination(String origin, String destination);

    @Query("SELECT * from stations where name = :name")
    Station getStationByName(String name);

    @Query("SELECT * from stations where address = :address")
    LiveData<Station> getStationByAddress(String address);

    @Query("SELECT * from stations where name = :name")
    LiveData<Station> getStationLiveDataByName(String name);

    @Update(onConflict = REPLACE)
    void updateStation(Station station);

    @Query("SELECT COUNT(*) from stations")
    int countStations();

    @Insert(onConflict = REPLACE)
    void save(Station station);
}

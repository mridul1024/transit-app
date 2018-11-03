package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StationDao {

    // Removes a station from the database
    @Delete
    void delete(Station station);

    @Query("SELECT * from stations where id = :id")
    Station getStationByID(int id);

    // Gets all stations from the database
    @Query("SELECT * from stations")
    List<Station> getAllStations();

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

    @Update(onConflict = REPLACE)
    void updateStation(Station station);

    @Query("SELECT COUNT(*) from stations")
    int countStations();

    @Insert(onConflict = REPLACE)
    void save(Station station);
}

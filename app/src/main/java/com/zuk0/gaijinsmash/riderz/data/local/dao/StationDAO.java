package com.zuk0.gaijinsmash.riderz.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.zuk0.gaijinsmash.riderz.data.local.entity.Station;

import java.util.List;

@Dao
public interface StationDAO {

    // Adds a station object to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addStation(Station station);

    // Removes a station from the database
    @Delete
    void delete(Station station);

    // Gets all stations from the database
    @Query("SELECT * from stations")
    public List<Station> getAllStations();

    // Select station by matching Longitude coordinate
    @Query("SELECT * from stations where longitude = :longitude")
    public Station getStationByLongitude(String longitude);

    // Selects station with the matching id
    @Query("SELECT * from stations where abbr = :abbr")
    public Station getStationByAbbr(String abbr);

    @Query("SELECT * from stations where name = :name")
    public Station getStationByName(String name);

    @Query("SELECT * from stations where address = :address")
    public Station getStationByAddress(String address);

    // Updates a station
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStation(Station station);

    // Get count
    @Query("SELECT COUNT(*) from stations")
    int countStations();
}

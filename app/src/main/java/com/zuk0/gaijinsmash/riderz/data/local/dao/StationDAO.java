package com.zuk0.gaijinsmash.riderz.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StationDAO {

    // Adds a station object to the database
    @Insert(onConflict = REPLACE)
    void addStation(Station station);

    // Removes a station from the database
    @Delete
    void delete(Station station);

    // Gets all stations from the database
    @Query("SELECT * from stations")
    public List<Station> getAllStations();

    @Query("SELECT * from stations")
    LiveData<List<Station>> getStationsLiveData();

    // Select station by matching Longitude coordinate
    @Query("SELECT * from stations where longitude = :longitude")
    public Station getStationByLongitude(String longitude);

    // Selects station with the matching id
    //todo: must eventually remove this when finished
    @Query("SELECT * from stations where abbr = :abbr")
    Station getStationByAbbr2(String abbr);

    @Query("SELECT * from stations where abbr = :abbr")
    LiveData<Station> getStationByAbbr(String abbr);

    @Query("SELECT * from stations where name = :name")
    public Station getStationByName(String name);

    @Query("SELECT * from stations where address = :address")
    LiveData<Station> getStationByAddress(String address);

    // Updates a station
    @Update(onConflict = REPLACE)
    void updateStation(Station station);

    // Get count
    @Query("SELECT COUNT(*) from stations")
    int countStations();

    @Insert(onConflict = REPLACE)
    void save(Station station);
}

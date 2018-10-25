package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TripDao {

    @Insert(onConflict = REPLACE)
    void save(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("SELECT * from trips where origin = :origin and destination = :destination")
    Trip load(String origin, String destination);

    @Query("SELECT * from trips where id = :id")
    Trip getTripById(int id);
}

package com.zuk0.gaijinsmash.riderz.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TripDao {

    @Insert(onConflict = REPLACE)
    void save(Trip trip);

    @Delete
    void delete(Trip trip);
}

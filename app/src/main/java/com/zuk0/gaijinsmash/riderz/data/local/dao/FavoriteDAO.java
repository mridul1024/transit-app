package com.zuk0.gaijinsmash.riderz.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;

import java.util.List;

@Dao
public interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void add(Favorite favorite);

    @Delete
    public void delete(Favorite favorite);

    @Query("SELECT * from favorites where destinationTrip = :originTrip OR originTrip = :originTrip")
    Favorite getFavorite(Trip originTrip);

    @Query("SELECT * from favorites")
    public LiveData<List<Favorite>> getAllFavoritesLiveData();

    @Query("SELECT COUNT(*) from favorites")
    int countFavorites();

    @Query("SELECT * from favorites where origin = :origin and destination = :destination")
    public Favorite getFavorite(String origin, String destination);

    @Query("SELECT * from favorites where priority = 1")
    public LiveData<List<Favorite>> getAllPriorityFavorites();

    //Update a priority favorite
    @Query("Update favorites set priority = 1 where id = :id")
    public void addPriorityById(int id);

    //Update priority to null
    @Query("Update favorites set priority = 0 where id = :id")
    public void removePriorityById(int id);

    @Query("SELECT * from favorites where originTrip = :originTrip or originTrip = :destTrip")
    LiveData<Favorite> findFavoriteByTrips(Trip originTrip, Trip destTrip);

    @Query("SELECT count(*) from favorites where priority = 1")
    int getPriorityCount();
}

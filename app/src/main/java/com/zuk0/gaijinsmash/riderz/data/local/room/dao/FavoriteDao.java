package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

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
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("SELECT * from favorites where id = :id")
    Favorite getFavoriteById(int id);

    @Query("SELECT * from favorites")
    LiveData<List<Favorite>> getAllFavoritesLiveData();

    @Query("SELECT COUNT(*) from favorites")
    int countFavorites();

    @Query("SELECT * from favorites where origin = :origin and destination = :destination")
    Favorite getFavorite(String origin, String destination);

    @Query("SELECT * from favorites where origin = :origin and destination = :destination " +
            "OR origin = :destination and destination = :origin")
    boolean doesFavoriteExist(String origin, String destination);

    @Query("SELECT * from favorites where priority = 1")
    LiveData<List<Favorite>> getAllPriorityFavorites();

    //Update a priority favorite
    @Query("Update favorites set priority = 1 where id = :id")
    void updatePriorityById(int id);

    //Update priority to null
    @Query("Update favorites set priority = 0 where id = :id")
    void removePriorityById(int id);

    @Query("SELECT * from favorites where origin = :origin and destination = :destination OR origin = :destination and destination = :origin")
    LiveData<Favorite> getLiveDataFavorite(String origin, String  destination);

    @Query("SELECT count(*) from favorites where priority = 1")
    int getPriorityCount();
}

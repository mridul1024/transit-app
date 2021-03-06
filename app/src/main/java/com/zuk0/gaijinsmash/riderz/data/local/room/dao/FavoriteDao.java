package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Maybe;

import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;

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
    Maybe<Favorite> getPriorityFavorite();

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

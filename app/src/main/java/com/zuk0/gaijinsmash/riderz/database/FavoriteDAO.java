package com.zuk0.gaijinsmash.riderz.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;

import java.util.List;

@Dao
public interface FavoriteDAO {
    // Adds a favorite object to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addStation(Favorite favorite);

    // Removes a favorite from the database
    @Delete
    public void delete(Favorite favorite);

    // Gets all favorites from the database
    @Query("SELECT * from favorites")
    public List<Favorite> getAllFavorites();

    @Query("SELECT * from favorites where origin = :origin")
    public List<Favorite> getFavoritesByOrigin(String origin);

    // Get count
    @Query("SELECT COUNT(*) from favorites")
    int countFavorites();

    @Query("SELECT * from favorites where origin = :origin and destination = :destination")
    public Favorite getFavorite(String origin, String destination);

    //Get selected priority
    @Query("SELECT * from favorites where priority = :priority")
    public List<Favorite> getAllFavoritesByPriority(Favorite.Priority priority);

    @Query("SELECT priority from favorites where id = :id")
    public int getPriorityById(int id);

    //Update a priority favorite
    @Query("Update favorites set priority = :priority where id = :id")
    public void setPriorityById(int id, Favorite.Priority priority);

    //Update priority to null
    @Query("Update favorites set priority = 0 where id = :id")
    public void removePriorityById(int id);

    @Query("SELECT * from favorites where origin = :origin and destination = :destination")
    public boolean isTripFavorited(String origin, String destination);

}

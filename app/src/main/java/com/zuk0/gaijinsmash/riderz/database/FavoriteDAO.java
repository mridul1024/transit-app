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
}

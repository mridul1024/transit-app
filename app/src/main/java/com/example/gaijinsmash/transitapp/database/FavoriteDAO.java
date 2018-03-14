package com.example.gaijinsmash.transitapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.gaijinsmash.transitapp.model.bart.Favorite;

import java.util.List;

@Dao
public interface FavoriteDAO {
    // Adds a favorite object to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addStation(Favorite favorite);

    // Removes a favorite from the database
    @Delete
    void delete(Favorite favorite);

    // Gets all favorites from the database
    @Query("SELECT * from favorites")
    public List<Favorite> getAllFavorites();

}

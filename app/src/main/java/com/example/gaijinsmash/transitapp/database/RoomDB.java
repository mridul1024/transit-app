package com.example.gaijinsmash.transitapp.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.gaijinsmash.transitapp.model.bart.Station;

@Database(entities = {Station.class /*, Example.class*/}, version = 1)
public abstract class RoomDB  extends RoomDatabase {
    public abstract StationDAO getStationDAO();

}

/*
To get an instance saved file in example-database

RoomDB db = Room.databaseBuilder(getApplicationContext(),
        RoomDB.class, "example-database").build();
 */
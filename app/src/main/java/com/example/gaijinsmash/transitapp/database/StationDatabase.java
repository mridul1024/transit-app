package com.example.gaijinsmash.transitapp.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.gaijinsmash.transitapp.model.bart.Station;

@Database(entities = {Station.class /*, Example.class*/}, version = 1)
public abstract class StationDatabase extends RoomDatabase {

    private static StationDatabase INSTANCE;

    public abstract StationDAO getStationDAO();

    // Room Builder
    public static StationDatabase getRoomDB(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StationDatabase.class, "station-database")
                    //.addMigrations(MIGRATION)
                    .fallbackToDestructiveMigration() //todo: need to change this
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    // Edit this to create a new migration for database - and use ".addMigrations(example)
    public static final Migration MIGRATION = new Migration(1,2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE stations ADD COLUMN last_update INTEGER");
        }
    };
}

/*
To get an instance saved file in example-database

StationDatabase db = Room.databaseBuilder(getApplicationContext(),
        StationDatabase.class, "example-database").build();
 */
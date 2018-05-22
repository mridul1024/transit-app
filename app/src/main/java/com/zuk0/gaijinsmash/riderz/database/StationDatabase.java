package com.zuk0.gaijinsmash.riderz.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.model.bart.Station;

@Database(entities = {Station.class}, version = 3, exportSchema = false)
public abstract class StationDatabase extends RoomDatabase {

    private static StationDatabase INSTANCE;

    public abstract StationDAO getStationDAO();

    //todo: add synchronized?
    public static StationDatabase getRoomDB(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StationDatabase.class, "stations")
                    .addMigrations(MIGRATION)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if(INSTANCE.isOpen()) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }

    // Edit this to create a new migration for database - and use ".addMigrations(example)
    private static final Migration MIGRATION = new Migration(1,2) {
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
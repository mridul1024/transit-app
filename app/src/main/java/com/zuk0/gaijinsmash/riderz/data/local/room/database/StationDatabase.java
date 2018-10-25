package com.zuk0.gaijinsmash.riderz.data.local.room.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;

@Database(entities = {Station.class}, version = 4, exportSchema = false)
public abstract class StationDatabase extends RoomDatabase {

    private static StationDatabase INSTANCE;

    public abstract StationDao getStationDAO();

    public static StationDatabase getRoomDB(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StationDatabase.class, "stations")
                    //.addMigrations(MIGRATION)
                    .fallbackToDestructiveMigrationFrom(3)
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
    /*
    private static final Migration MIGRATION = new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE stations ADD COLUMN last_update INTEGER ");
        }
    };
    */
}
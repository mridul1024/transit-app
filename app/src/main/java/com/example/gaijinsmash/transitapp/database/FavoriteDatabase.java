package com.example.gaijinsmash.transitapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.gaijinsmash.transitapp.model.bart.Favorite;

@Database(entities = {Favorite.class}, version = 4, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase INSTANCE;
    public abstract FavoriteDAO getFavoriteDAO();

    public static FavoriteDatabase getRoomDB(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoriteDatabase.class, "favorite-database")
                    .addMigrations(MIGRATION)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
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

FavoriteDatabase db = Room.databaseBuilder(getApplicationContext(),
        FavoriteDatabase.class, "example-database").build();
*/

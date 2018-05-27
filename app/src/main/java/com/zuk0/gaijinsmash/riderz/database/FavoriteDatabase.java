package com.zuk0.gaijinsmash.riderz.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;

@Database(entities = {Favorite.class}, version = 13, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase INSTANCE;
    public abstract FavoriteDAO getFavoriteDAO();

    //todo: add synchronized?
    public static FavoriteDatabase getRoomDB(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoriteDatabase.class, "favorites")
                    .addMigrations(MIGRATION)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    // Edit this to create a new migration for database
    private static final Migration MIGRATION = new Migration(13,14) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE favorites ADD COLUMN last_update INTEGER");
        }
    };
}

/*
To get an instance saved file in example-database

FavoriteDatabase db = Room.databaseBuilder(getApplicationContext(),
        FavoriteDatabase.class, "example-database").build();
*/

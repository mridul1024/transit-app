package com.zuk0.gaijinsmash.riderz.data.local.room.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.room.converter.Converters;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.FavoriteDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;

@Database(entities = {Favorite.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase INSTANCE;
    public abstract FavoriteDao getFavoriteDAO();

    public static FavoriteDatabase getRoomDB(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoriteDatabase.class, "favorites")
                    //.addMigrations(MIGRATION)
                    .fallbackToDestructiveMigration() //todo remove this after testing
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    // Edit this to create a new migration for database
    private static final Migration MIGRATION = new Migration(1,2) {
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

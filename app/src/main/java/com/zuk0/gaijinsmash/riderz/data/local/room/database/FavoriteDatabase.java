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

@Database(entities = {Favorite.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase INSTANCE;
    public abstract FavoriteDao getFavoriteDAO();

    public static FavoriteDatabase getRoomDB(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoriteDatabase.class, "favorites")
                    .addMigrations(MIGRATION)
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /*
        Edit this to create a new migration for database
     */
    private static final Migration MIGRATION = new Migration(2,3) {

        @Override
        public void migrate(SupportSQLiteDatabase database) {

            // remove the old table
            database.execSQL("DROP TABLE favorites");

            // create a new table
            database.execSQL("CREATE TABLE favorites (id INTEGER, a TEXT, b TEXT," +
                    " trainHeaderStations TEXT, system TEXT, description TEXT, priority INTEGER, colors TEXT, PRIMARY KEY(id))");

            // copy the data
            /*
            database.execSQL("INSERT INTO favorites_temp (id, a, b, trainHeaderStations, system, description, priority, colors) "
                    + "SELECT id, origin, destination, trainHeaderStations, system, description, priority, colors "
                    + "FROM favorites");
                    */



            // change the table name to the correct one
            //database.execSQL("ALTER TABLE favorites_temp RENAME TO favorites");
        }
    };
}

/*
To get an instance saved file in example-database

FavoriteDatabase db = Room.databaseBuilder(getApplicationContext(),
        FavoriteDatabase.class, "example-database").build();
*/

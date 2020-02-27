package com.zuk0.gaijinsmash.riderz.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.room.converter.Converters
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.FavoriteDao

@Database(entities = [Favorite::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDAO() : FavoriteDao

    companion object {
        private var INSTANCE: FavoriteDatabase? = null
        fun getRoomDB(context: Context): FavoriteDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "favorites") //.addMigrations(MIGRATION)
                        .fallbackToDestructiveMigration() //destroy db on upgrade
                        .build()
            }
            return INSTANCE as FavoriteDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        /*
        Edit this to create a new migration for database
     */
        private val MIGRATION: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) { // create a new table
                database.execSQL("CREATE TABLE favorites_temp (id INTEGER, a TEXT, b TEXT," +
                        " trainHeaderStations TEXT, system TEXT, description TEXT, priority INTEGER, colors TEXT, PRIMARY KEY(id))")
                // copy the data
/*
            database.execSQL("INSERT INTO favorites_temp (id, a, b, trainHeaderStations, system, description, priority, colors) "
                    + "SELECT id, origin, destination, trainHeaderStations, system, description, priority, colors "
                    + "FROM favorites");
                    */
// remove the old table
                database.execSQL("DROP TABLE favorites")
                // change the table name to the correct one
                database.execSQL("ALTER TABLE favorites_temp RENAME TO favorites")
            }
        }
    }
}
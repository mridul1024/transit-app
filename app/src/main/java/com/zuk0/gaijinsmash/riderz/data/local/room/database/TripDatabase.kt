package com.zuk0.gaijinsmash.riderz.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.data.local.room.converter.Converters
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao

@Database(entities = [Trip::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TripDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao

    companion object {

        private var INSTANCE: TripDatabase? = null

        fun getRoomDB(context: Context): TripDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, TripDatabase::class.java, "trips")
                        .addMigrations(MIGRATION)
                        .build()
            }
            return INSTANCE as TripDatabase
        }

        fun destroyInstance() {
            if (INSTANCE!!.isOpen) {
                INSTANCE!!.close()
            }
            INSTANCE = null
        }

        // Edit this to create a new migration for database - and use ".addMigrations(example)
        private val MIGRATION: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE trips ADD COLUMN last_update INTEGER")
            }
        }
    }
}
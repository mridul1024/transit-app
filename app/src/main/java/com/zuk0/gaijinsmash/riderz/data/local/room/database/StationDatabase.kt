package com.zuk0.gaijinsmash.riderz.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao
import javax.inject.Inject

@Database(entities = [Station::class], version = 6, exportSchema = false)
abstract class StationDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao

    companion object {
        private var INSTANCE: StationDatabase? = null
        fun getRoomDB(context: Context): StationDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, StationDatabase::class.java, "stations")
                        .addMigrations(MIGRATION)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE as StationDatabase
        }

        fun destroyInstance() {
            if (INSTANCE?.isOpen == true) {
                INSTANCE?.close()
            }
            INSTANCE = null
        }

        // Edit this to create a new migration for database - and use ".addMigrations(example)
        private val MIGRATION: Migration = object : Migration(4, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE stations ADD COLUMN message TEXT")
                database.execSQL("ALTER TABLE stations ADD COLUMN error TEXT")
            }
        }
    }
}
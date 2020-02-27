package com.zuk0.gaijinsmash.riderz.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.converter.Converters
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao

@Database(entities = [EtdXmlResponse::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EtdDatabase : RoomDatabase() {
    abstract fun etdDao(): EtdDao

    companion object {
        private var INSTANCE: EtdDatabase? = null
        fun getRoomDb(context: Context): EtdDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, EtdDatabase::class.java, "estimates")
                        .addMigrations(MIGRATION)
                        .build()
            }
            return INSTANCE as EtdDatabase
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
                database.execSQL("ALTER TABLE estimates ADD COLUMN last_update INTEGER")
            }
        }
    }
}
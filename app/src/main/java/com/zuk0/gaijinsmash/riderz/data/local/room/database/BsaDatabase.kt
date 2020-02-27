package com.zuk0.gaijinsmash.riderz.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.room.converter.Converters
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao
import javax.inject.Inject

@Database(entities = [BsaXmlResponse::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BsaDatabase : RoomDatabase() {

    abstract fun bsaDao(): BsaDao

    companion object {
        private var INSTANCE: BsaDatabase? = null
        fun getRoomDb(context: Context): BsaDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, BsaDatabase::class.java, "advisories")
                        .addMigrations(MIGRATION)
                        .build()
            }
            return INSTANCE as BsaDatabase
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
                database.execSQL("ALTER TABLE advisories ADD COLUMN last_update INTEGER")
            }
        }
    }
}
package com.zuk0.gaijinsmash.riderz.di.module

import android.app.Application
import android.content.Context
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao
import com.zuk0.gaijinsmash.riderz.data.local.room.database.BsaDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.EtdDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.TripDatabase

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

/*
   This module will provide the dependencies for the overall application level
   This module will provide the dependencies for the overall application level
   @Singleton is application scope
*/
@Module
class AppModule {

    @Provides
    fun provideContext(application: Application) : Context {
        return application
    }

    @Provides
    fun provideStationDatabase(context: Context): StationDatabase {
        return StationDatabase.getRoomDB(context)
    }

    @Provides
    fun provideStationDao(db: StationDatabase): StationDao {
        return db.stationDao()
    }

    @Provides
    fun provideBsaDatabase(context: Context): BsaDatabase {
        return BsaDatabase.getRoomDb(context)
    }

    @Provides
    fun provideBsaDao(db: BsaDatabase): BsaDao {
        return db.bsaDao()
    }

    @Provides
    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    fun provideEtdDatabase(context: Context): EtdDatabase {
        return EtdDatabase.getRoomDb(context)
    }

    @Provides
    fun provideEtdDao(db: EtdDatabase): EtdDao {
        return db.etdDao()
    }

    @Provides
    fun provideTripDatabase(context: Context): TripDatabase {
        return TripDatabase.getRoomDB(context)
    }

    @Provides
    fun provideTripDao(db: TripDatabase): TripDao {
        return db.tripDao()
    }
}

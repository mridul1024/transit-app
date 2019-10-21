package com.zuk0.gaijinsmash.riderz.di.module

import android.content.Context
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao
import com.zuk0.gaijinsmash.riderz.data.local.room.database.BsaDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.EtdDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.TripDatabase
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * place all fragment scoped injections here
 */
@Module
class FragmentModule {

    @Provides
    @Singleton
     fun provideBsaDatabase(context: Context): BsaDatabase {
        return BsaDatabase.getRoomDb(context)
    }

    @Provides
    @Singleton
     fun provideBsaDao(db: BsaDatabase): BsaDao {
        return db.bsaDAO
    }

    @Provides
    @Singleton
     fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    @Singleton
     fun provideStationDatabase(context: Context): StationDatabase {
        return StationDatabase.getRoomDB(context)
    }

    @Provides
    @Singleton
     fun provideStationDao(db: StationDatabase): StationDao {
        return db.stationDAO
    }

    @Provides
    @Singleton
     fun provideEtdDatabase(context: Context): EtdDatabase {
        return EtdDatabase.getRoomDb(context)
    }

    @Provides
    @Singleton
     fun provideEtdDao(db: EtdDatabase): EtdDao {
        return db.etdDao
    }

    @Provides
    @Singleton
     fun provideTripDatabase(context: Context): TripDatabase {
        return TripDatabase.getRoomDB(context)
    }

    @Provides
    @Singleton
     fun provideTripDao(db: TripDatabase): TripDao {
        return db.tripDao
    }
}
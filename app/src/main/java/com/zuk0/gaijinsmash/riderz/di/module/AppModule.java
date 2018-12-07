package com.zuk0.gaijinsmash.riderz.di.module;

import android.app.Application;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.BsaDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.EtdDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.TripDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
/*
    This module will provide the dependencies for the overall application level
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideApplication(Application application) {
        return application;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory() {
        return new ViewModelProvider.NewInstanceFactory();
    }

    @Provides
    @Singleton
    BsaDatabase provideBsaDatabase(Context context) {
        return BsaDatabase.getRoomDb(context);
    }

    @Provides
    @Singleton
    BsaDao provideBsaDao(BsaDatabase db) {
        return db.getBsaDAO();
    }

    @Provides
    @Singleton
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    StationDatabase provideStationDatabase(Context context) { return StationDatabase.getRoomDB(context); }

    @Provides
    @Singleton
    StationDao provideStationDao(StationDatabase db) { return db.getStationDAO(); }

    @Provides
    @Singleton
    EtdDatabase provideEtdDatabase(Context context) { return EtdDatabase.getRoomDb(context); }

    @Provides
    @Singleton
    EtdDao provideEtdDao(EtdDatabase db) { return db.getEtdDao(); }

    @Provides
    @Singleton
    TripDatabase provideTripDatabase(Context context) { return TripDatabase.getRoomDB(context); }

    @Provides
    @Singleton
    TripDao provideTripDao(TripDatabase db) { return db.getTripDao(); }
}

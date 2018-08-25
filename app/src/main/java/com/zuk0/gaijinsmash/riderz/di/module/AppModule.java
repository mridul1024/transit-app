package com.zuk0.gaijinsmash.riderz.di.module;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.local.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.local.dao.StationDAO;
import com.zuk0.gaijinsmash.riderz.data.local.database.BsaDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.database.EtdDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;

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
    ViewModelProvider.Factory provideViewModelFactory() {
        return new ViewModelProvider.NewInstanceFactory();
    }

    @Provides
    @Singleton
    StationDatabase provideStationDatabase(Context context) { return StationDatabase.getRoomDB(context); }

    @Provides
    @Singleton
    StationDAO provideStationDao(StationDatabase db) { return db.getStationDAO(); }

    @Provides
    @Singleton
    EtdDatabase provideEtdDatabase(Context context) { return EtdDatabase.getRoomDb(context); }

    @Provides
    @Singleton
    EtdDao provideEtdDao(EtdDatabase db) { return db.getEtdDao(); }
}

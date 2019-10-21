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
    @Singleton is application scope
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideApplication(Application application) {
        return application;
    }

}

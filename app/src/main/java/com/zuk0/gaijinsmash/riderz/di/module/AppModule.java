package com.zuk0.gaijinsmash.riderz.di.module;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.local.database.BsaDatabase;

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
}

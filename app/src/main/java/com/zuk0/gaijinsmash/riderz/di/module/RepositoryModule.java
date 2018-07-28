package com.zuk0.gaijinsmash.riderz.di.module;

import android.arch.persistence.room.Room;

import com.zuk0.gaijinsmash.riderz.RiderzApplication;
import com.zuk0.gaijinsmash.riderz.data.local.database.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.local.database.BsaDatabase;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Inject
    RiderzApplication mApplication;

    @Provides
    @Singleton
    BsaRepository provideBsaRepository(BsaRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public BsaDao provideBsaDao(BsaDatabase bsaDatabase) {
        return bsaDatabase.getBsaDAO();
    }

    @Provides
    @Singleton
    public BsaDatabase provideBsaDatabase() {
        return Room.databaseBuilder(mApplication, BsaDatabase.class,"advisories").allowMainThreadQueries().build();
    }
}

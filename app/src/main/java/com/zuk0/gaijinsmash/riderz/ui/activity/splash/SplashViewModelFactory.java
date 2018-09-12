package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

import javax.inject.Inject;

public class SplashViewModelFactory implements ViewModelProvider.Factory {

    private final StationRepository repository;
    private final Application application;

    @Inject
    SplashViewModelFactory(Application application, StationRepository repository) {
        this.application = application;
        this.repository = repository;
    }

    @NonNull
    @Override
    public SplashViewModel create(@NonNull Class modelClass) {
        return new SplashViewModel(application, repository);
    }
}

package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

public class SplashViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    @Inject
    SplashViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public SplashViewModel create(@NonNull Class modelClass) {
        return new SplashViewModel(application);
    }
}

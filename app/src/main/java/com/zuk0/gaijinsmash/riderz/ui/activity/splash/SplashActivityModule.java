package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import android.app.Application;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashActivityModule {

    @Provides
    SplashViewModel provideSplashViewModel(Application application, StationRepository repository) {
        return new SplashViewModel(application, repository);
    }
}

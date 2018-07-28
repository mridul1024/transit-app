package com.zuk0.gaijinsmash.riderz.di.module;

import android.app.Application;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivityComponent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
/*
    This module will provide the dependencies for the overall application level
 */
@Module(subcomponents = {MainActivityComponent.class})
public class AppModule {

    @Provides
    @Singleton
    Context provideApplication(Application application) {
        return application;
    }
}

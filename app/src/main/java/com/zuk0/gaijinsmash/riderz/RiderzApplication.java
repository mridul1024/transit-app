package com.zuk0.gaijinsmash.riderz;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.di.component.AppComponent;
import com.zuk0.gaijinsmash.riderz.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/*
    This will create Dagger object
    https://stackoverflow.com/questions/47906538/subcomponent-builder-is-missing-setters
    https://proandroiddev.com/dagger-2-component-builder-1f2b91237856
 */
public class RiderzApplication extends Application implements HasActivityInjector{

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        appComponent.inject(this);
        Log.i("onCreate", "app component initialized");
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}

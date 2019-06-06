package com.zuk0.gaijinsmash.riderz;

import android.app.Activity;
import android.app.Application;
import android.os.Parcelable;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.zuk0.gaijinsmash.riderz.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/*
    This will create a Dagger object
 */
public class RiderzApplication extends Application implements HasActivityInjector{

    private static final boolean ENABLE_LEAKCANARY = false;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
        Log.i("onCreate", "app component initialized");

        if(ENABLE_LEAKCANARY) {
            if(LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        } else {
            Logger.d("LeakCanary is disabled");
        }
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    private void initData() {

    }
}

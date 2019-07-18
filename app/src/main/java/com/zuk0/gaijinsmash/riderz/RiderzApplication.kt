package com.zuk0.gaijinsmash.riderz

import android.app.Activity
import android.app.Application
import android.os.Parcelable
import android.util.Log
import com.crashlytics.android.Crashlytics

import com.orhanobut.logger.Logger
import com.squareup.leakcanary.LeakCanary
import com.zuk0.gaijinsmash.riderz.di.component.DaggerAppComponent

import javax.inject.Inject

import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric

class RiderzApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
        Logger.i( "app component initialized")
        initLeakCanary()
        initCrashlytics()
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    private fun initLeakCanary() {
        if (ENABLE_LEAKCANARY) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return
            }
            LeakCanary.install(this)
        } else {
            Logger.d("LeakCanary is disabled")
        }
    }

    private fun initCrashlytics() {
        if(!BuildConfig.DEBUG)
            Fabric.with(this, Crashlytics())
    }

    private fun initData() {
        //todo
    }

    companion object {
        private const val ENABLE_LEAKCANARY = false
    }
}

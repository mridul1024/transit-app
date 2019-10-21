package com.zuk0.gaijinsmash.riderz

import android.app.Activity
import android.app.Application
import android.os.Parcelable
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.orhanobut.logger.AndroidLogAdapter

import com.orhanobut.logger.Logger
import com.squareup.leakcanary.LeakCanary
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.di.component.DaggerAppComponent
import dagger.android.AndroidInjector

import javax.inject.Inject

import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.fabric.sdk.android.Fabric

class RiderzApplication : Application(), HasAndroidInjector {

    @Inject lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return activityDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
        initLogger()
        initLeakCanary()
        initCrashlytics()
        //initMockData()
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
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

    private fun initMockData() {
        val favoriteDb = FavoriteDatabase.getRoomDB(this)
        val stationDb = StationDatabase.getRoomDB(this)
        val a = stationDb.stationDAO.getStationByName("Ashby")
        val b  = stationDb.stationDAO.getStationByName("Montgomery")
        val favorite = Favorite()

        favorite.a = a
        favorite.b = b
        favorite.priority = Favorite.Priority.ON
        favoriteDb.favoriteDAO.save(Favorite())
    }

    companion object {
        private const val ENABLE_LEAKCANARY = false
    }
}

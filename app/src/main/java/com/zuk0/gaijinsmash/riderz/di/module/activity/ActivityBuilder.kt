package com.zuk0.gaijinsmash.riderz.di.module.activity

import com.zuk0.gaijinsmash.riderz.di.annotation.ActivityScope
import com.zuk0.gaijinsmash.riderz.di.module.activity.fragment.FragmentBuilder
import com.zuk0.gaijinsmash.riderz.di.module.activity.main.MainActivityModule
import com.zuk0.gaijinsmash.riderz.di.module.activity.splash.SplashActivityModule
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import com.zuk0.gaijinsmash.riderz.ui.activity.splash.SplashActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivity(): SplashActivity
}

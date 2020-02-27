package com.zuk0.gaijinsmash.riderz.di.component


import android.app.Application

import com.zuk0.gaijinsmash.riderz.RiderzApplication
import com.zuk0.gaijinsmash.riderz.di.module.activity.ActivityBuilder
import com.zuk0.gaijinsmash.riderz.di.module.AppModule
import com.zuk0.gaijinsmash.riderz.di.module.NetModule
import com.zuk0.gaijinsmash.riderz.di.module.activity.fragment.FragmentBuilder
import com.zuk0.gaijinsmash.riderz.di.module.viewmodel.ViewModelModule

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

/*
    This is a bridge between @Module and @Inject
    https://blog.mindorks.com/the-new-dagger-2-android-injector-cbe7d55afa6a
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivityBuilder::class,
    FragmentBuilder::class,
    ViewModelModule::class,
    AppModule::class,
    NetModule::class])
interface AppComponent {

    fun inject(application: RiderzApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

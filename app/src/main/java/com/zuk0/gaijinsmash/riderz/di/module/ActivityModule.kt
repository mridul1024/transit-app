package com.zuk0.gaijinsmash.riderz.di.module

import android.content.Context
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.presenter.HomeWeatherPresenter
import dagger.Module
import dagger.Provides

/**
 * place all activity scoped injections here
 */
@Module(includes = [FragmentBuilder::class])
class ActivityModule {

    @Provides
    fun provideHomeWeatherPresenter(context: Context) : HomeWeatherPresenter {
        return HomeWeatherPresenter(context)
    }
}
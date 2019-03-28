package com.zuk0.gaijinsmash.riderz.ui.activity.main

import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideMainViewModel() : MainViewModel {
        return MainViewModel();
    }
}
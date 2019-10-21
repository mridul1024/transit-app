package com.zuk0.gaijinsmash.riderz.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuk0.gaijinsmash.riderz.di.annotations.ViewModelKey
import com.zuk0.gaijinsmash.riderz.di.factory.RiderzViewModelFactory
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(mainViewModel: MainViewModel) : ViewModel

    @Binds
    fun bindViewModelFactory(factory: RiderzViewModelFactory): ViewModelProvider.Factory
}
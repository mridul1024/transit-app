package com.zuk0.gaijinsmash.riderz.di.module.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuk0.gaijinsmash.riderz.di.annotation.ViewModelKey
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map.BartMapViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results.BartResultsViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite.FavoritesViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map.GoogleMapViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info.StationInfoViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations.StationsViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    fun provideFavoritesViewModel(favoritesViewModel: FavoritesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BartMapViewModel::class)
    fun provideBartMapViewModel(bartMapViewModel: BartMapViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GoogleMapViewModel::class)
    fun provideGoogleMapViewModel(googleMapViewModel: GoogleMapViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TripViewModel::class)
    fun provideTripViewModel(tripViewModel: TripViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StationInfoViewModel::class)
    fun provideStationInfoViewModel(stationsViewModel: StationInfoViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StationsViewModel::class)
    fun provideStationsViewModel(stationsViewModel: StationsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BartResultsViewModel::class)
    fun provideBartResultsViewModel(bartResultsViewModel: BartResultsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun provideHomeViewModel(homeViewModel: HomeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(mainViewModel: MainViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideSplashViewModel(splashViewModel: SplashViewModel) : ViewModel

    @Binds
    fun bindViewModelFactory(factory: RiderzViewModelFactory): ViewModelProvider.Factory

}

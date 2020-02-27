package com.zuk0.gaijinsmash.riderz.di.module.activity.fragment

import com.zuk0.gaijinsmash.riderz.di.annotation.FragmentScope
import com.zuk0.gaijinsmash.riderz.di.module.activity.fragment.home.HomeFragmentModule
import com.zuk0.gaijinsmash.riderz.di.module.activity.fragment.stations.StationsModule
import com.zuk0.gaijinsmash.riderz.di.module.viewmodel.ViewModelModule
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map.BartMapFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results.BartResultsFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite.FavoritesFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map.GoogleMapFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info.StationInfoFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations.StationsFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    fun contributeHomeFragment() : HomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeStationInfoFragment() : StationInfoFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [StationsModule::class])
    fun contributeStationsFragment() : StationsFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeTripFragment() : TripFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeBartResultsFragment() : BartResultsFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeGoogleMapFragment() : GoogleMapFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeBartMapFragmentFragment() : BartMapFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeFavoritesFragmentt() : FavoritesFragment
}
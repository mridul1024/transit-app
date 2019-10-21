package com.zuk0.gaijinsmash.riderz.di.module

import com.zuk0.gaijinsmash.riderz.di.annotations.FragmentScope
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
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun contributeHomeFragment() : HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [StationInfoFragment::class])
    fun contributeStationInfoFragment() : StationInfoFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [StationsFragment::class])
    fun contributeStationsFragment() : StationsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [TripFragment::class])
    fun contributeTripFragment() : TripFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [BartResultsFragment::class])
    fun contributeBartResultsFragment() : BartResultsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [GoogleMapFragment::class])
    fun contributeGoogleMapFragment() : GoogleMapFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [BartMapFragment::class])
    fun contributeBartMapFragmentFragment() : BartMapFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FavoritesFragment::class])
    fun contributeFavoritesFragmentt() : FavoritesFragment
}
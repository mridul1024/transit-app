package com.zuk0.gaijinsmash.riderz.di.module;

import com.zuk0.gaijinsmash.riderz.di.annotations.ActivityScope;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;
import com.zuk0.gaijinsmash.riderz.ui.activity.splash.SplashActivity;
import com.zuk0.gaijinsmash.riderz.ui.activity.splash.SplashActivityModule;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map.BartMapFragmentProvider;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results.BartResultsFragmentProvider;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite.FavoritesFragmentProvider;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map.GoogleMapFragmentProvider;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info.StationInfoFragmentProvider;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations.StationsFragmentProvider;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragmentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityModule.class})
    abstract MainActivity bindMainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = { SplashActivityModule.class })
    abstract SplashActivity bindSplashActivity();
}

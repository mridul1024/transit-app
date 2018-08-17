package com.zuk0.gaijinsmash.riderz.di.module;

import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeFragmentProvider;
import com.zuk0.gaijinsmash.riderz.ui.fragment.station_info.StationInfoFragmentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {
            HomeFragmentProvider.class,
            StationInfoFragmentProvider.class})
    abstract MainActivity bindMainActivity();
}
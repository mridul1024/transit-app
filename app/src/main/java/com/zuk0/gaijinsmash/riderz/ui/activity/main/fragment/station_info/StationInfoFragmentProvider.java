package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class StationInfoFragmentProvider {
    @ContributesAndroidInjector(modules = StationInfoFragmentModule.class)
    abstract StationInfoFragment provideStationInfoFragment();
}

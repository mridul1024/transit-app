package com.zuk0.gaijinsmash.riderz.ui.fragment.stations;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class StationsFragmentProvider {
    @ContributesAndroidInjector(modules = StationsFragmentModule.class)
    abstract StationsFragment provideStationsFragment();
}


package com.zuk0.gaijinsmash.riderz.ui.fragment.trip;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TripFragmentProvider {
    @ContributesAndroidInjector(modules = TripFragmentModule.class)
    abstract TripFragment provideTripFragment();
}

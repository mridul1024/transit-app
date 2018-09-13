package com.zuk0.gaijinsmash.riderz.ui.fragment.google_map;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GoogleMapFragmentProvider {
    @ContributesAndroidInjector(modules = GoogleMapFragmentModule.class)
    abstract GoogleMapFragment provideGoogleMapFragment();

}

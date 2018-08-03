package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BartMapProvider {
    @ContributesAndroidInjector(modules = BartMapFragmentModule.class)
    abstract BartMapFragment provideBartMapFragement();
}

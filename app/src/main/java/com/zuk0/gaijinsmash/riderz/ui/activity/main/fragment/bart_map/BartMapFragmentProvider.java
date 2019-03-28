package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BartMapFragmentProvider {
    @ContributesAndroidInjector(modules = BartMapFragmentModule.class)
    abstract BartMapFragment provideBartMapFragement();
}

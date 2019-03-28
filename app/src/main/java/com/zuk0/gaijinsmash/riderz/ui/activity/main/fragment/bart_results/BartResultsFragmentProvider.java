package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BartResultsFragmentProvider {
    @ContributesAndroidInjector(modules = BartResultsFragmentModule.class)
    abstract BartResultsFragment provideBartResultsFragment();
}

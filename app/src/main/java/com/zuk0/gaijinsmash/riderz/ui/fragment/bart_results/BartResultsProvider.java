package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BartResultsProvider {
    @ContributesAndroidInjector(modules = BartResultsFragmentModule.class)
    abstract BartResultsFragment provideBartResultsFragment();
}

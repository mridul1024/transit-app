package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import dagger.Module;
import dagger.Provides;

@Module
public class BartResultsFragmentModule {
    @Provides
    BartResultsViewModel provideBartResultsViewModel() { return new BartResultsViewModel(); }
}

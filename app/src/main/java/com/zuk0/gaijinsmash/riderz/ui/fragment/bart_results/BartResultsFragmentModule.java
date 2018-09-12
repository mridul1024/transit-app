package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class BartResultsFragmentModule {

    @Provides
    BartResultsViewModel provideBartResultsViewModel(TripRepository repository) {
        return new BartResultsViewModel(repository);
    }
}

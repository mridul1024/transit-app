package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results;

import android.app.Application;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class BartResultsFragmentModule {

    @Provides
    BartResultsViewModel provideBartResultsViewModel(Application application, TripRepository repository) {
        return new BartResultsViewModel(application, repository);
    }
}

package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import javax.inject.Inject;

//todo: think about abstracting ViewModelProvider.Factory with Factory pattern
public class BartResultsViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final TripRepository tripRepository;

    @Inject
    BartResultsViewModelFactory(Application application, TripRepository tripRepository) {
        this.application = application;
        this.tripRepository = tripRepository;
    }

    @NonNull
    @Override
    public BartResultsViewModel create(@NonNull Class modelClass) {
        return new BartResultsViewModel(application, tripRepository);
    }
}

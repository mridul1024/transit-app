package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import javax.inject.Inject;

//todo: think about abstracting ViewModelProvider.Factory with Factory pattern
public class BartResultsViewModelFactory implements ViewModelProvider.Factory {

    private final TripRepository tripRepository;

    @Inject
    BartResultsViewModelFactory(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @NonNull
    @Override
    public BartResultsViewModel create(@NonNull Class modelClass) {
        return new BartResultsViewModel(tripRepository);
    }
}

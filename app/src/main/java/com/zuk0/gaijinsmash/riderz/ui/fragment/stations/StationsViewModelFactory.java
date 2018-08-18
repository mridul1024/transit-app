package com.zuk0.gaijinsmash.riderz.ui.fragment.stations;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.repository.StationRepository;

import javax.inject.Inject;

public class StationsViewModelFactory implements ViewModelProvider.Factory {

    private final StationRepository repository;

    @Inject
    public StationsViewModelFactory(StationRepository repository) { this.repository = repository; }

    @NonNull
    @Override
    public StationsViewModel create(@NonNull Class modelClass) {
        return new StationsViewModel(repository);
    }
}

package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

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

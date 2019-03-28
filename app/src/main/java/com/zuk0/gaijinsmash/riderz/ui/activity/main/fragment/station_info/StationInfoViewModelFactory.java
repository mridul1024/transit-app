package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

import javax.inject.Inject;

public class StationInfoViewModelFactory implements ViewModelProvider.Factory {

    private final StationRepository repository;

    @Inject
    public StationInfoViewModelFactory(StationRepository repository) { this.repository = repository; }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public StationInfoViewModel create(@NonNull Class modelClass) { return new StationInfoViewModel(repository); }
}

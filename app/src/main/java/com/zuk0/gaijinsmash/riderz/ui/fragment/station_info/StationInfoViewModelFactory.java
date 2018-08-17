package com.zuk0.gaijinsmash.riderz.ui.fragment.station_info;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.repository.StationRepository;

import javax.inject.Inject;

public class StationInfoViewModelFactory implements ViewModelProvider.Factory {

    private final StationRepository repository;

    @Inject
    public StationInfoViewModelFactory(StationRepository repository) { this.repository = repository; }

    @NonNull
    @Override
    public StationInfoViewModel create(@NonNull Class modelClass) { return new StationInfoViewModel(repository); }
}

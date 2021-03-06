package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class StationsFragmentModule {
    @Provides
    StationsViewModel provideStationViewModel(StationRepository repository) {
        return new StationsViewModel(repository);
    }
}

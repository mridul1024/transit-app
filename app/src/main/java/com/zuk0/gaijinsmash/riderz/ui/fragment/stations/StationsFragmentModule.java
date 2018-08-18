package com.zuk0.gaijinsmash.riderz.ui.fragment.stations;

import com.zuk0.gaijinsmash.riderz.data.repository.StationRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class StationsFragmentModule {
    @Provides
    StationsViewModel provideStationViewModel(StationRepository repository) {
        return new StationsViewModel(repository);
    }
}

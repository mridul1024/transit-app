package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class StationInfoFragmentModule {

    @Provides
    StationInfoViewModel provideStationInfoViewModel(StationRepository repository) {
        return new StationInfoViewModel(repository);
    }
}

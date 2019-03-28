package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip;

import dagger.Module;
import dagger.Provides;

@Module
public class TripFragmentModule {
    @Provides
    TripViewModel provideTripViewModel() {
        return new TripViewModel();
    }
}

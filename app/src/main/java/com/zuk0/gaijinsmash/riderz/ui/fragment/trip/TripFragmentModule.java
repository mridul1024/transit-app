package com.zuk0.gaijinsmash.riderz.ui.fragment.trip;

import dagger.Module;
import dagger.Provides;

@Module
public class TripFragmentModule {
    @Provides
    TripViewModel provideTripViewModel() {
        return new TripViewModel();
    }
}

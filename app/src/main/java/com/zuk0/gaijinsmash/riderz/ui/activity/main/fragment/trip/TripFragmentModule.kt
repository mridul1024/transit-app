package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip

import dagger.Module
import dagger.Provides

@Module
class TripFragmentModule {
    @Provides
    fun provideTripViewModel(): TripViewModel {
        return TripViewModel()
    }
}
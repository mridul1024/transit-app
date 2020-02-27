package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TripFragmentProvider {
    @ContributesAndroidInjector(modules = [TripFragmentModule::class])
    abstract fun provideTripFragment(): TripFragment?
}
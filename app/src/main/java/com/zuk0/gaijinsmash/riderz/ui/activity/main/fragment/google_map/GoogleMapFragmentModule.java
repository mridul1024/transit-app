package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class GoogleMapFragmentModule {
    @Provides
    GoogleMapViewModel provideGoogleMapViewModel(Application application) { return new GoogleMapViewModel(application); }
}

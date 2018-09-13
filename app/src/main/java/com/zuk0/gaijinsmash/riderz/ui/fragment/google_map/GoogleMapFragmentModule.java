package com.zuk0.gaijinsmash.riderz.ui.fragment.google_map;

import dagger.Module;
import dagger.Provides;

@Module
public class GoogleMapFragmentModule {
    @Provides
    GoogleMapViewModel provideGoogleMapViewModel() { return new GoogleMapViewModel(); }
}

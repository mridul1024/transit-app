package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class BartMapFragmentModule {
    @Provides
    BartMapViewModel provideBartMapViewModel(Application application) {
        return new BartMapViewModel(application);
    }
}

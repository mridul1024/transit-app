package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import dagger.Module;
import dagger.Provides;

@Module
public class BartMapFragmentModule {
    @Provides
    BartMapViewModel provideBartMapViewModel() {
        return new BartMapViewModel();
    }
}

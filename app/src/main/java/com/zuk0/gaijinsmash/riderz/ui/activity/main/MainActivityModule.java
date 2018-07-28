package com.zuk0.gaijinsmash.riderz.ui.activity.main;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    @Provides
    MainViewModel provideMainViewModel() {
        return new MainViewModel();
    }
}

package com.zuk0.gaijinsmash.riderz.ui.activity.main;

import android.arch.lifecycle.ViewModelProvider;


import com.zuk0.gaijinsmash.riderz.ui.ViewModelProviderFactory;
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeFragment;
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeFragmentModule;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public class MainActivityModule {

    @Provides
    ViewModelProvider.Factory provideMainViewModel(MainViewModel mainViewModel) {
        return new ViewModelProviderFactory<>(mainViewModel);
    }
}

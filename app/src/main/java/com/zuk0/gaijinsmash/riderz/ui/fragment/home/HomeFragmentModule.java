package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@Module
public class HomeFragmentModule {

    @Provides
    HomeViewModel provideHomeViewModel(BsaRepository repository) {
        return new HomeViewModel(repository);
    }
}

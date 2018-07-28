package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeFragmentModule {

    @Inject
    BsaRepository repository;

    @Provides
    HomeViewModel provideHomeViewModel() {
        return new HomeViewModel(repository);
    }
}

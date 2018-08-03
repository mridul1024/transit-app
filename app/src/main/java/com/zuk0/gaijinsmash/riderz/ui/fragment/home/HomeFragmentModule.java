package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import com.zuk0.gaijinsmash.riderz.data.repository.BsaRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeFragmentModule {

    @Provides
    HomeViewModel provideHomeViewModel(BsaRepository repository) {
        return new HomeViewModel(repository);
    }
}

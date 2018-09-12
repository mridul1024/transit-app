package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeFragmentModule {

    @Provides
    HomeViewModel provideHomeViewModel(BsaRepository bsaRepository, EtdRepository etdRepository) {
        return new HomeViewModel(bsaRepository, etdRepository);
    }
}

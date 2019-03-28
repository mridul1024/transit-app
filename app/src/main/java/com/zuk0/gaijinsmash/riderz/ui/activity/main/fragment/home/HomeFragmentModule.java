package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home;

import android.app.Application;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeFragmentModule {

    @Provides
    HomeViewModel provideHomeViewModel(Application application, TripRepository tripRepository, BsaRepository bsaRepository, EtdRepository etdRepository) {
        return new HomeViewModel(application, tripRepository, bsaRepository, etdRepository);
    }
}

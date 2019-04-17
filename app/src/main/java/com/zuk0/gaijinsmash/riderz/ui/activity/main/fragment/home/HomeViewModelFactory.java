package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.WeatherRepository;

import javax.inject.Inject;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final BsaRepository bsaRepository;
    private final EtdRepository etdRepository;
    private final TripRepository tripRepository;
    private final WeatherRepository weatherRepository;

    @Inject
    public HomeViewModelFactory(Application application,
                                TripRepository tripRepository,
                                BsaRepository bsaRepository,
                                EtdRepository etdRepository,
                                WeatherRepository weatherRepository) {
        this.application = application;
        this.tripRepository = tripRepository;
        this.bsaRepository = bsaRepository;
        this.etdRepository = etdRepository;
        this.weatherRepository = weatherRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public HomeViewModel create(@NonNull Class modelClass) {
        return new HomeViewModel(application, tripRepository, bsaRepository, etdRepository, weatherRepository);
    }
}

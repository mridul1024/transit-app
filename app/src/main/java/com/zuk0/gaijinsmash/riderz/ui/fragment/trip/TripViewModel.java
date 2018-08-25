package com.zuk0.gaijinsmash.riderz.ui.fragment.trip;

import android.arch.lifecycle.ViewModel;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TripViewModel extends ViewModel {

    private TripRepository mTripRepository;

    @Inject
    public TripViewModel(TripRepository repository) {
        this.mTripRepository = repository;
    }

    public void getTrip() {
        mTripRepository.getTrip();
    }
}

package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;

import javax.inject.Inject;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final BsaRepository bsaRepository;
    private final EtdRepository etdRepository;

    @Inject
    public HomeViewModelFactory(BsaRepository bsaRepository, EtdRepository etdRepository) {
        this.bsaRepository = bsaRepository;
        this.etdRepository = etdRepository;
    }

    @NonNull
    @Override
    public HomeViewModel create(@NonNull Class modelClass) {
        return new HomeViewModel(bsaRepository, etdRepository);
    }
}

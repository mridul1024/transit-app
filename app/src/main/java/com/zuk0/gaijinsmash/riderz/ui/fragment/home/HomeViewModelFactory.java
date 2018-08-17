package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.repository.BsaRepository;

import javax.inject.Inject;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final BsaRepository repository;

    @Inject
    public HomeViewModelFactory(BsaRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public HomeViewModel create(@NonNull Class modelClass) {
        return new HomeViewModel(repository);
    }
}

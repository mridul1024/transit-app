package com.zuk0.gaijinsmash.riderz.ui.fragment.favorite;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;

import javax.inject.Inject;

public class FavoritesViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    @Inject
    public FavoritesViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public FavoritesViewModel create(@NonNull Class modelClass) {
        return new FavoritesViewModel(application);
    }
}

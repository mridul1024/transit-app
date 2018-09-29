package com.zuk0.gaijinsmash.riderz.ui.fragment.google_map;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NonNls;

import javax.inject.Inject;

public class GoogleMapViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    @Inject
    public GoogleMapViewModelFactory(Application application) { this.application = application; }

    @NonNull
    @Override
    public GoogleMapViewModel create(@NonNull Class modelClass) {
        return new GoogleMapViewModel(application);
    }
}

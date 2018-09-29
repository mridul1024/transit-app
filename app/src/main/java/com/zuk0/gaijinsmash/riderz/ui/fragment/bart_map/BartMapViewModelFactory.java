package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import dagger.Module;

@Module
public class BartMapViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    @Inject
    public BartMapViewModelFactory(Application application) { this.application = application; }

    @NonNull
    @Override
    public BartMapViewModel create(@NonNull Class modelClass) {
        return new BartMapViewModel(application);
    }


}

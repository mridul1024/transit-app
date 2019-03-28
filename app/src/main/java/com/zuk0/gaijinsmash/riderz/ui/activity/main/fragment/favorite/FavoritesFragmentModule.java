package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class FavoritesFragmentModule {

    @Provides
    FavoritesViewModel provideFavoritesViewModel(Application application) { return new FavoritesViewModel(application); }

}

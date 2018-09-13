package com.zuk0.gaijinsmash.riderz.ui.fragment.favorite;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FavoritesFragmentProvider {
    @ContributesAndroidInjector(modules = FavoritesFragmentModule.class)
    abstract FavoritesFragment provideFavoritesFragment();
}

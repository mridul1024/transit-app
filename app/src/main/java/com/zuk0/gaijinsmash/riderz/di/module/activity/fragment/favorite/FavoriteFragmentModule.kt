package com.zuk0.gaijinsmash.riderz.di.module.activity.fragment.favorite

import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite.FavoritesViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
interface FavoriteFragmentModule {

    @ContributesAndroidInjector
    fun contributeFavoriteViewModel(): FavoritesViewModel
}
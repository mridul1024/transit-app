package com.zuk0.gaijinsmash.riderz.di.module;

import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeFragment;
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeFragmentModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
/*
    Parent module for all subcomponents of Fragment related stuff
 */
@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector(modules = HomeFragmentModule.class)
    abstract HomeFragment bindHomeFragment();

    /*
    Alternative method

    @Binds
    @IntoMap
    abstract AndroidInjector.Factory<? extends Fragment>
    bindHomeFragment(HomeFragmentSubComponent.Builder builder);

    would have to create a subccomponent
    @Subcomponent(modules = MainActivityModule.class)
    public interface MainActivityComponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity {}
     */

}

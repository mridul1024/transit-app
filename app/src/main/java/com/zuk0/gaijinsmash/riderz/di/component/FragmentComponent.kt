package com.zuk0.gaijinsmash.riderz.di.component

import androidx.fragment.app.Fragment
import com.zuk0.gaijinsmash.riderz.di.annotations.FragmentScope
import com.zuk0.gaijinsmash.riderz.di.module.FragmentModule
import dagger.Component


@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: Fragment)
}
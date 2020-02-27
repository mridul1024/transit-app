package com.zuk0.gaijinsmash.riderz.di.component

import androidx.fragment.app.Fragment
import com.zuk0.gaijinsmash.riderz.di.annotation.FragmentScope
import com.zuk0.gaijinsmash.riderz.di.module.activity.fragment.FragmentModule
import dagger.Component


@FragmentScope
@Component(dependencies = [ActivityComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: Fragment)
}
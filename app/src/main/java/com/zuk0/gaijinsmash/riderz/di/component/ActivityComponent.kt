package com.zuk0.gaijinsmash.riderz.di.component

import android.app.Activity
import com.zuk0.gaijinsmash.riderz.di.annotation.ActivityScope
import com.zuk0.gaijinsmash.riderz.di.module.activity.ActivityModule
import dagger.Component

/**
 * This component is a child of AppComponent
 */
@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: Activity)
}
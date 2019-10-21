package com.zuk0.gaijinsmash.riderz.di.component

import androidx.appcompat.app.AppCompatActivity
import com.zuk0.gaijinsmash.riderz.di.annotations.ActivityScope
import com.zuk0.gaijinsmash.riderz.di.module.ActivityModule
import com.zuk0.gaijinsmash.riderz.di.module.ViewModelModule
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class, ViewModelModule::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)
}
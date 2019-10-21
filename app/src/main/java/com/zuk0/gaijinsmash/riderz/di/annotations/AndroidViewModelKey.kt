package com.zuk0.gaijinsmash.riderz.di.annotations

import androidx.lifecycle.AndroidViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class AndroidViewModelKey(val value: KClass<out AndroidViewModel>)
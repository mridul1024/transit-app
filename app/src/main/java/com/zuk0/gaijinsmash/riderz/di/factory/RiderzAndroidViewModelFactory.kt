package com.zuk0.gaijinsmash.riderz.di.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class RiderzAndroidViewModelFactory @Inject constructor(
        private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>, application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[viewModelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (viewModelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown view model class $viewModelClass")
        }
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
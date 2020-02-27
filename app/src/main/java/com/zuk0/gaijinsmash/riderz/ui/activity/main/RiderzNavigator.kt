package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

// 1
@Navigator.Name("rider")
class RiderzNavigator: Navigator<RiderzNavigator.Destination>() {

    // 2
    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Extras?): NavDestination? {
        Log.d(TAG, "navigate()")
        return null
    }
    // 3
    override fun createDestination(): Destination {
        return Destination(this)
    }

    // 4
    override fun popBackStack(): Boolean {
        return false
    }

    // 5
    class Destination(riderzNavigator: RiderzNavigator) :
            NavDestination(riderzNavigator)

    companion object {
        private const val TAG = "RiderzNavigator"
    }
}
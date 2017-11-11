package com.example.gaijinsmash.transitapp.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.example.gaijinsmash.transitapp.R
import com.example.gaijinsmash.transitapp.activity.MainActivity

/**
 * Created by ryanj on 11/9/2017.
 */

class Demno : AppCompatActivity(), AHBottomNavigation.OnTabSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bottomNav = findViewById<View>(R.id.bottom_navigation) as AHBottomNavigation
        bottomNav.setOnTabSelectedListener { position, wasSelected ->
            //TODO: do something cool here
            true
        }
        bottomNav.setOnNavigationPositionListener {
            //TODO: manage the new x position
        }
    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        return false
    }
}

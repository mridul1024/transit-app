package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.*
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.utils.NetworkUtils
import com.zuk0.gaijinsmash.riderz.ui.fragment.about.AboutFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map.BartMapFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.favorite.FavoritesFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.google_map.GoogleMapFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.help.HelpFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.phone_lines.PhoneLinesFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.settings.SettingsFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.stations.StationsFragment
import com.zuk0.gaijinsmash.riderz.ui.fragment.trip.TripFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector{
    @Inject
    lateinit var fragmentInjector : DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment>? {
        return fragmentInjector
    }

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    // ---------------------------------------------------------------------------------------------
    // Lifecycle Events
    // ---------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.main_activity)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        navController = findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(bottom_navigation, navController)
        setupWithNavController(nav_view, navController)
        mAppBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.homeFragment, R.id.googleMapFragment, R.id.tripFragment, R.id.favoritesFragment))
                .setDrawerLayout(drawer_layout)
                .build()
        setupWithNavController(toolbar, navController, mAppBarConfiguration)

        if(savedInstanceState == null) {
            //todo: add logic
        }
    }

    override fun onSupportNavigateUp() = navController.popBackStack()

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Navigation Settings
    // ---------------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }
}



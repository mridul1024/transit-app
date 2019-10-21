package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
import com.zuk0.gaijinsmash.riderz.di.factory.RiderzViewModelFactory
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.presenter.HomeWeatherPresenter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HasAndroidInjector {

    @Inject lateinit var fragmentInjector : DispatchingAndroidInjector<Any>

    @Inject lateinit var weatherPresenter: HomeWeatherPresenter

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector
    }

    private lateinit var binding: MainActivityBinding
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : MainViewModel

    // ---------------------------------------------------------------------------------------------
    // Lifecycle Events
    // ---------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController = findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(main_bottom_navigation, navController)
        setupWithNavController(nav_view, navController)
        mAppBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.homeFragment, R.id.googleMapFragment, R.id.tripFragment, R.id.favoritesFragment))
                .setDrawerLayout(drawer_layout)
                .build()
        setupWithNavController(toolbar, navController, mAppBarConfiguration)

        if(savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)
        }
        initCityBackground()
        initWeather()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onSupportNavigateUp() : Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            handleBottomNavViewBehaviour()
            navController.navigateUp()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    private fun handleBottomNavViewBehaviour() {
        HideBottomViewOnScrollBehavior<BottomNavigationView>(this, null).slideUp(main_bottom_navigation)
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
        handleBottomNavViewBehaviour()
        return true
    }

    //TODO check if bottom nav is hidden, and show on new transitions
    /**
     * Helpers
     */
    private fun initCityBackground() {
        Glide.with(this)
                .load(R.drawable.sf_skyline)
                .into(binding.mainBannerImageView)

        binding.imageBackground.background = viewModel.getBackgroundDrawable(this, viewModel.hour)
        binding.mainCollapsingToolbar.setExpandedTitleColor(viewModel.getColorScheme(this, viewModel.hour))
        binding.mainCollapsingToolbar.setCollapsedTitleTextColor(viewModel.getColorScheme(this, viewModel.hour))
        binding.widgetWeather.findViewById<TextView>(R.id.weather_name_tv)?.setTextColor(viewModel.getColorScheme(this, viewModel.hour))
        binding.widgetWeather.findViewById<TextView>(R.id.weather_humidity_tv)?.setTextColor(viewModel.getColorScheme(this, viewModel.hour))
        binding.widgetWeather.findViewById<TextView>(R.id.weather_temp_tv)?.setTextColor(viewModel.getColorScheme(this, viewModel.hour))
        binding.widgetWeather.findViewById<TextView>(R.id.weather_wind_tv)?.setTextColor(viewModel.getColorScheme(this, viewModel.hour))
    }
    private fun initWeather() {
        weatherPresenter.viewModel = viewModel
        lifecycle.addObserver(weatherPresenter)
    }
}



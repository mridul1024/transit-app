package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
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


    private lateinit var binding: MainActivityBinding
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val mViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java)}

    // ---------------------------------------------------------------------------------------------
    // Lifecycle Events
    // ---------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        Logger.addLogAdapter(AndroidLogAdapter())
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

        if(savedInstanceState == null) {
            //todo: add logic
        }
        initPic()
    }

    private fun initPic() {
        mViewModel.initPic(this, mViewModel.getHour(), main_banner_imageView)
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
            navController.popBackStack()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun handleBottomNavViewBehaviour() {
        HideBottomViewOnScrollBehavior<BottomNavigationView>(this, null).slideUp(main_bottom_navigation)
    }

    private fun initFab() {
        binding.mainFab.setOnClickListener {
            //todo

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
        handleBottomNavViewBehaviour()
        return true
    }

    //TODO check if bottom nav is hidden, and show on new transitions
}



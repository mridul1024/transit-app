package com.example.gaijinsmash.transitapp.activity

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.gaijinsmash.transitapp.R
import com.example.gaijinsmash.transitapp.fragment.*
import com.example.gaijinsmash.transitapp.network.CheckInternet

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mCurrentFragment = "HomeFragment"
    private val mFragmentManager = fragmentManager
    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavigationView: NavigationView
    private lateinit var mBottomNavigation: AHBottomNavigation

    // ---------------------------------------------------------------------------------------------
    // Lifecycle Events
    // ---------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        initBottomNavBar()
        initNavigationDrawer(toolbar)
        if(savedInstanceState == null) {
            initHomeFragment()
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    // ---------------------------------------------------------------------------------------------
    // Navigation
    // ---------------------------------------------------------------------------------------------
    private fun initBottomNavBar() {
        mBottomNavigation = findViewById<View>(R.id.bottom_navigation) as AHBottomNavigation
        val item1 = AHBottomNavigationItem(R.string.bottomnav_title_0, R.drawable.ic_menu_home, R.color.colorPrimaryDark)
        val item2 = AHBottomNavigationItem(R.string.bottomnav_title_1, R.drawable.ic_menu_map, R.color.colorPrimaryDark)
        val item3 = AHBottomNavigationItem(R.string.bottomnav_title_2, R.drawable.ic_menu_schedule, R.color.colorPrimaryDark)
        val item4 = AHBottomNavigationItem(R.string.bottomnav_title_3, R.drawable.ic_menu_favorite, R.color.colorPrimaryDark)

        mBottomNavigation.addItem(item1)
        mBottomNavigation.addItem(item2)
        mBottomNavigation.addItem(item3)
        mBottomNavigation.addItem(item4)
        mBottomNavigation.currentItem = 0
        mBottomNavigation.setOnTabSelectedListener { position, _ ->
            when(position) {
                0 -> initHomeFragment()
                1 -> initMapFragment()
                2 -> initScheduleFragment()
                3 -> initFavoritesFragment()
            }
            true
        }
        mBottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
    }

    private fun initHomeFragment() {
        replaceFrag(HomeFragment(), "HomeFragment")
        mNavigationView.setCheckedItem(R.id.nav_home)
    }

    private fun initMapFragment() {
        if(CheckInternet.isNetworkActive(applicationContext)) {
            replaceFrag(GoogleMapFragment(), "GoogleMapFragment")
        } else {
            replaceFrag(BartMapFragment(), "BartMapFragment")
        }
        mNavigationView.setCheckedItem(R.id.nav_map)
    }

    private fun initScheduleFragment() {
        replaceFrag(TripFragment(), "TripFragment")
        mNavigationView.setCheckedItem(R.id.nav_schedule)
    }

    private fun initFavoritesFragment() {
        replaceFrag(FavoritesFragment(), "FavoritesFragment")
    }

    private fun replaceFrag(newFrag:Fragment, tag: String) {
        mFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContent, newFrag, tag)
                //.addToBackStack(null)
                .commit()
        mCurrentFragment = tag
    }

    private fun initNavigationDrawer(toolbar:Toolbar) {
        mDrawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()
        mNavigationView = findViewById<View>(R.id.nav_view) as NavigationView
        mNavigationView.setNavigationItemSelectedListener(this)
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
        val id = item.itemId
        if (id == R.id.action_settings) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContent, SettingsFragment())
                    .addToBackStack(null)
                    .commit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                return if(mCurrentFragment.equals("HomeFragment")) {
                    closeDrawer()
                    false
                } else {
                    fragmentFactory(HomeFragment(), "HomeFragment", false)
                    mBottomNavigation.currentItem = 0
                    true
                }
            }
            R.id.nav_map -> {
                if(CheckInternet.isNetworkActive(applicationContext))
                    fragmentFactory(GoogleMapFragment(), "GoogleMapFragment", false) else fragmentFactory(BartMapFragment(), "BartMapFragment", false)
                mBottomNavigation.currentItem = 1
                return true
            }
            R.id.nav_schedule -> {
                fragmentFactory(TripFragment(), "TripFragment", false)
                mBottomNavigation.currentItem = 2
                return true
            }
            R.id.nav_station -> {
                fragmentFactory(StationFragment(), "StationFragment", true)
                return true
            }
            R.id.nav_help -> {
                fragmentFactory(HelpFragment(), "HelpFragment", true)
                //todo: resolve setCheckedItem issue with nav menus
                return true
            }
            R.id.nav_about -> {
                fragmentFactory(AboutFragment(), "AboutFragment", true)
                return true
            }
            R.id.nav_phoneLines -> {
                fragmentFactory(PhoneLinesFragment(), "PhoneLinesFragment", true)
                return true
            }
        }
        return true
    }

    private fun fragmentFactory(fragment: Fragment, tag: String, backstackFlag: Boolean) {
        if(backstackFlag) {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContent, fragment, tag)
                    .addToBackStack(null)
                    .commit()
            mCurrentFragment = tag
        } else {
            replaceFrag(fragment, tag)
        }
        closeDrawer()
    }

    private fun closeDrawer() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
    }
}



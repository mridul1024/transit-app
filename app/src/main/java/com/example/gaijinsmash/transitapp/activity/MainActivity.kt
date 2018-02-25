package com.example.gaijinsmash.transitapp.activity

import android.app.Fragment
import android.content.Intent
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

    //todo: add FAVORITES Module
    var mCurrentFragment = "HomeFragment"
    val mFragmentManager = fragmentManager
    lateinit var mDrawer: DrawerLayout
    lateinit var mNavigationView: NavigationView
    lateinit var mBottomNavigation: AHBottomNavigation

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

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume();
    }

    override fun onPause() {
        super.onPause();
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
    fun initBottomNavBar() {
        mBottomNavigation = findViewById<View>(R.id.bottom_navigation) as AHBottomNavigation
        val item1 = AHBottomNavigationItem(R.string.bottomnav_title_0, R.drawable.ic_menu_home, R.color.colorPrimaryDark)
        val item2 = AHBottomNavigationItem(R.string.bottomnav_title_1, R.drawable.ic_menu_map, R.color.colorPrimaryDark)
        val item3 = AHBottomNavigationItem(R.string.bottomnav_title_2, R.drawable.ic_menu_schedule, R.color.colorPrimaryDark)

        mBottomNavigation.addItem(item1)
        mBottomNavigation.addItem(item2)
        mBottomNavigation.addItem(item3)
        mBottomNavigation.setCurrentItem(0)
        mBottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            when(position) {
                0 -> initHomeFragment()
                1 -> initMapFragment()
                2 -> initScheduleFragment()
            }
            true
        }
    }

    fun initHomeFragment() {
        replaceFrag(HomeFragment(), "HomeFragment")
        mNavigationView.setCheckedItem(R.id.nav_home)
    }

    fun initMapFragment() {
        if(CheckInternet.isNetworkActive(applicationContext)) {
            replaceFrag(BartMapFragment(), "BartMapFragment")
        } else {
            replaceFrag(MapFragment(), "MapFragment")
        }
        mNavigationView.setCheckedItem(R.id.nav_map)
    }

    fun initScheduleFragment() {
        replaceFrag(TripFragment(), "TripFragment")
        mNavigationView.setCheckedItem(R.id.nav_schedule)
    }

    fun initStationFragment() {
        replaceFrag(StationFragment(), "StationFragment")
        mNavigationView.setCheckedItem(R.id.nav_station)
    }

    fun replaceFrag(newFrag:Fragment, tag: String) {
        mFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContent, newFrag, tag)
                //.addToBackStack(null)
                .commit()
        mCurrentFragment = tag
    }

    fun initNavigationDrawer(toolbar:Toolbar) {
        mDrawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()
        mNavigationView = findViewById<View>(R.id.nav_view) as NavigationView
        mNavigationView.setNavigationItemSelectedListener(this)
    }

    /*
    fun initFAB() {
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
    */

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
                if(mCurrentFragment.equals("HomeFragment")) {
                    closeDrawer()
                    return false
                } else {
                    fragmentFactory(HomeFragment(), "HomeFragment")
                    mBottomNavigation.setCurrentItem(0)
                    return true
                }
            }
            R.id.nav_map -> {
                if(CheckInternet.isNetworkActive(applicationContext))
                    fragmentFactory(BartMapFragment(), "BartMapFragment") else fragmentFactory(MapFragment(), "MapFragment")
                mBottomNavigation.setCurrentItem(1)
                return true
            }
            R.id.nav_schedule -> {
                fragmentFactory(TripFragment(), "TripFragment")
                mBottomNavigation.setCurrentItem(2)
                return true
            }
            R.id.nav_station -> {
                fragmentFactory(StationFragment(), "StationFragment")
                return true
            }
            R.id.nav_help -> {
                fragmentFactory(HelpFragment(), "HelpFragment")
                //todo: resolve setCheckedItem issue with nav menus
                return true
            }
            R.id.nav_about -> {
                fragmentFactory(AboutFragment(), "AboutFragment")
                //todo: resolve setCheckedItem issue with nav menus
                return true
            }
        }
        return true
    }

    fun fragmentFactory(fragment: Fragment, tag: String) {
        replaceFrag(fragment, tag)
        closeDrawer()
    }

    fun closeDrawer() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
    }

    fun shareSomething() {
        // Create intent
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.setType("text/plain")
        //intent.setType("img")
        val shareText = "Here is the share content"
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shared from TransitApp")
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(intent, "Share via"))
    }
}



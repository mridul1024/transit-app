package com.example.gaijinsmash.transitapp.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.gaijinsmash.transitapp.R
import com.example.gaijinsmash.transitapp.fragment.*
import com.example.gaijinsmash.transitapp.utils.CheckInternet

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var currentFragment = "HomeFragment"
    val fragmentManager = supportFragmentManager
    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var bottomNavigation: AHBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Initialize navigation menus
        initBottomNavBar()
        initNavigationDrawer(toolbar)

        // Initialize Fragments
        //initFragments()
        initDefaultFrag()
    }

    fun initFragments() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(HomeFragment(), "HomeFragment")
        fragmentTransaction.add(BartMapFragment(), "BartMapFragment")
        fragmentTransaction.add(MapFragment(), "MapFragment")
        fragmentTransaction.add(StationFragment(), "StationFragment")
        fragmentTransaction.add(ScheduleFragment(), "ScheduleFragment")
        fragmentTransaction.commitAllowingStateLoss() // todo: check if this is appropriate method
    }

    fun initDefaultFrag() {
        replaceFrag(HomeFragment(), "HomeFragment")
    }
    // ---------------------------------------------------------------------------------------------
    // Navigation
    // ---------------------------------------------------------------------------------------------
    fun initBottomNavBar() {
        bottomNavigation = findViewById<View>(R.id.bottom_navigation) as AHBottomNavigation
        val item1 = AHBottomNavigationItem(R.string.bottomnav_title_0, R.drawable.ic_menu_home, R.color.colorPrimaryDark)
        val item2 = AHBottomNavigationItem(R.string.bottomnav_title_1, R.drawable.ic_menu_map, R.color.colorPrimaryDark)
        val item3 = AHBottomNavigationItem(R.string.bottomnav_title_2, R.drawable.ic_menu_schedule, R.color.colorPrimaryDark)

        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)
        bottomNavigation.setCurrentItem(0)
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
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
        //navigationView.setCheckedItem(R.id.nav_home)
    }

    fun initMapFragment() {
        if(CheckInternet.isNetworkActive(applicationContext)) {
            replaceFrag(BartMapFragment(), "BartMapFragment")
        } else {
            replaceFrag(MapFragment(), "MapFragment")
        }
        navigationView.setCheckedItem(R.id.nav_map)
    }

    fun initScheduleFragment() {
        replaceFrag(ScheduleFragment(), "ScheduleFragment")
        navigationView.setCheckedItem(R.id.nav_schedule)
    }

    fun initStationFragment() {
        replaceFrag(StationFragment(), "StationFragment")
        navigationView.setCheckedItem(R.id.nav_station)
    }

    fun replaceFrag(newFrag:Fragment, tag: String) {
        //if(currentFrag != "HomeFragment") fragmentManager.popBackStackImmediate()
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContent, newFrag, tag)
                .commit()
        currentFragment = tag
    }

    fun initNavigationDrawer(toolbar:Toolbar) {
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
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
    // Lifecycle Events
    // ---------------------------------------------------------------------------------------------
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
        if(fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                fragmentFactory(HomeFragment(), "HomeFragment")
                bottomNavigation.setCurrentItem(0)
                return true
            }
            R.id.nav_map -> {
                if(CheckInternet.isNetworkActive(applicationContext))
                    fragmentFactory(BartMapFragment(), "BartMapFragment") else fragmentFactory(MapFragment(), "MapFragment")
                bottomNavigation.setCurrentItem(1)
                return true
            }
            R.id.nav_schedule -> {
                fragmentFactory(ScheduleFragment(), "ScheduleFragment")
                bottomNavigation.setCurrentItem(2)
                return true
            }
            R.id.nav_station -> {
                fragmentFactory(StationFragment(), "StationFragment")
                return true
            }
            R.id.nav_share -> {
                // TODO: add logic
                Toast.makeText(applicationContext, "Share", Toast.LENGTH_SHORT).show();
            }
            R.id.nav_send -> {
                // TODO: add logic
                Toast.makeText(applicationContext, "Send", Toast.LENGTH_SHORT).show();
            }
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun fragmentFactory(fragment: Fragment, tag: String) {
        replaceFrag(fragment, tag)
        //todo: maintain position with bottomnav

        // Closes the Navigation Drawer
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
    }
}



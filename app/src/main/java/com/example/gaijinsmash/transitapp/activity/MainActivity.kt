package com.example.gaijinsmash.transitapp.activity

import android.app.PendingIntent.getActivity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
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
import com.example.gaijinsmash.transitapp.activity.fragment.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        initBottomNavBar()
        initNavigationDrawer(toolbar)

        // Loads HomeFragment by default
        val tx = getSupportFragmentManager().beginTransaction()
        tx.replace(R.id.fragmentContent, HomeFragment())
        tx.commit()
    }

    // ---------------------------------------------------------------------------------------------
    //
    // ---------------------------------------------------------------------------------------------
    fun initBottomNavBar() {
        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as AHBottomNavigation
        val item1 = AHBottomNavigationItem(R.string.bottomnav_title_0, R.drawable.ic_menu_home, R.color.colorPrimaryDark)
        val item2 = AHBottomNavigationItem(R.string.bottomnav_title_1, R.drawable.ic_menu_map, R.color.colorPrimaryDark)
        val item3 = AHBottomNavigationItem(R.string.bottomnav_title_2, R.drawable.ic_menu_schedule, R.color.colorPrimaryDark)
        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)
        bottomNavigation.setCurrentItem(0)
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"))
        bottomNavigation.setColoredModeColors(getColor(R.color.activeColor), getColor(R.color.inactiveColor)) //todo: is this working?
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            when(position) {
                0 -> startHomeFragment()
                1 -> startMapFragment()
                2 -> startScheduleFragment()
            }
            true
        }
        bottomNavigation.setOnNavigationPositionListener {
            //TODO: manage the new x/y position
        }
    }

    fun startHomeFragment() {
        val homeFrag = HomeFragment()
        replaceFrag(homeFrag)
    }
    fun startMapFragment() {
        val mapFrag = BartMapFragment()
        replaceFrag(mapFrag)
    }
    fun startScheduleFragment() {
        val scheduleFrag = ScheduleFragment()
        replaceFrag(scheduleFrag)
    }

    fun replaceFrag(newFrag:Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContent, newFrag)
                .addToBackStack(newFrag.toString())
                .commit()
    }

    fun initNavigationDrawer(toolbar:Toolbar) {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
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
    // User Action Handling Events
    // ---------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        val count = fragmentManager.backStackEntryCount
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (count > 0) {
            // todo: check logic
            // if on HomeFrag - clean stack - do not add stack, back button exits app.
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Navigation Settings
    // ---------------------------------------------------------------------------------------------

    //TODO : when either nav menu is selected - the relevant item is also selected

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
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                fragmentFactory(HomeFragment())
                return true
            }
            R.id.nav_schedule -> {
                fragmentFactory(ScheduleFragment())
                return true
            }
            R.id.nav_station -> {
                fragmentFactory(StationFragment())
                return true
            }
            R.id.nav_map -> {
                fragmentFactory(MapFragment())
                return true
            }
            R.id.nav_share -> {
                // TODO: add logic
            }
            R.id.nav_send -> {
                // TODO: add logic
            }
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun fragmentFactory(fragment: Fragment) {
        // Changes the fragment view
        val fragmentManager = getSupportFragmentManager()
        fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).addToBackStack(null).commit()
        // Closes the Navigation Drawer
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
    }
}



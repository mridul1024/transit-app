package com.example.gaijinsmash.transitapp.activity

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.gaijinsmash.transitapp.R
import com.example.gaijinsmash.transitapp.R.id.nav_home
import com.example.gaijinsmash.transitapp.database.StationDatabase
import com.example.gaijinsmash.transitapp.fragment.*
import com.example.gaijinsmash.transitapp.utils.CheckInternet

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var mCurrentFragment = "HomeFragment"
    val mFragmentManager = supportFragmentManager
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
        if(mCurrentFragment.equals("HomeFragment")) finish()
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
        replaceFrag(ScheduleFragment(), "ScheduleFragment")
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
                .setTransition(1)
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
        if(mFragmentManager.backStackEntryCount > 0) {
            mFragmentManager.popBackStack()
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
                mBottomNavigation.setCurrentItem(0)
                return true
            }
            R.id.nav_map -> {
                if(CheckInternet.isNetworkActive(applicationContext))
                    fragmentFactory(BartMapFragment(), "BartMapFragment") else fragmentFactory(MapFragment(), "MapFragment")
                mBottomNavigation.setCurrentItem(1)
                return true
            }
            R.id.nav_schedule -> {
                fragmentFactory(ScheduleFragment(), "ScheduleFragment")
                mBottomNavigation.setCurrentItem(2)
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
        //replaceFrag(fragment, tag)

        mFragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment, tag).addToBackStack(null).commit()
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
    }
    // ---------------------------------------------------------------------------------------------
    // AsyncTask
    // ---------------------------------------------------------------------------------------------
    private class CreateDatabaseTask: AsyncTask<Void, Void, Boolean> {
        lateinit var mContext: Context
        constructor(context: Context) {
            mContext = context;
        }

        override fun doInBackground(vararg p0: Void?): Boolean {
            var result = false;

            return result
        }

        override fun onPostExecute(result: Boolean) {
            if(result) Log.i("CreateDatabaseTask:", "DB created")
        }
    }
}



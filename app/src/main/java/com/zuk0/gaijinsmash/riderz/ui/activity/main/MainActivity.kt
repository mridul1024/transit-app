package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.app.AlertDialog
import android.content.Intent
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.event.EventToggleMap
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bottom_sheet.ActionBottomDialogFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.camera.CameraFragment.Companion.KEY_EVENT_ACTION
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.camera.CameraFragment.Companion.KEY_EVENT_EXTRA
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.main_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject
import kotlin.math.min

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HasAndroidInjector {

    @Inject lateinit var fragmentInjector : DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector
    }

    private lateinit var binding: MainActivityBinding
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : MainViewModel

    private lateinit var weatherPresenter: MainWeatherPresenter

    // Train map view
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private var trainMapIv: ImageView? = null


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
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        initFab()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
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
            showBottomNavigation(true)
            navController.navigateUp()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //todo permissions request for camera
        super.onActivityResult(requestCode, resultCode, data)
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun showBottomNavigation(enable: Boolean) {
        if(enable)
            HideBottomViewOnScrollBehavior<BottomNavigationView>(this, null).slideUp(main_bottom_navigation)
        else
            HideBottomViewOnScrollBehavior<BottomNavigationView>(this, null).slideDown(main_bottom_navigation)
    }

    // ---------------------------------------------------------------------------------------------
    // Navigation Settings
    // ---------------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        showBottomNavigation(false)
        return true
    }

    private fun initFab() {
        binding.fabOpen.visibility = View.VISIBLE
        binding.fabOpen.setOnClickListener {
            binding.fabOpen.isExpanded = !fabOpen.isExpanded
            animateFAB(fabOpen.isExpanded)
            //todo set background alpha
            binding.mainShadowOverlay.visibility = View.VISIBLE
        }
        binding.fabClose.setOnClickListener {
            binding.fabOpen.isExpanded = !fabOpen.isExpanded
            animateFAB(fabOpen.isExpanded)
            binding.mainShadowOverlay.visibility = View.GONE
        }
    }

    private fun animateFAB(isExpanded: Boolean) {
        if(isExpanded) {
            binding.fabOpen.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
            binding.fabClose.visibility = View.VISIBLE
            binding.fabClose.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
            binding.fabCamera.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_open)
            binding.fabComplain.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_open)
            binding.fabMap.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_open)
        } else {
            binding.fabClose.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_backward)
            binding.fabClose.visibility = View.INVISIBLE
            binding.fabOpen.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_backward)
            binding.fabCamera.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_close)
            binding.fabComplain.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_close)
            binding.fabMap.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_close)
        }
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
        binding.toolbar.setTitleTextColor(viewModel.getColorScheme(this, viewModel.hour))

        if(viewModel.isDaytime) {

        }

        if(viewModel.isNightTime) {

        }
        //todo set hamburger button color
        //binding.toolbar.navigationIcon?.colorFilter = ColorFilter()
        //todo set color of options menu
    }

    private fun initWeather() {
        weatherPresenter = MainWeatherPresenter(this, viewModel, binding)
        lifecycle.addObserver(weatherPresenter)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, min(scaleFactor, 20.0f))
            trainMapIv?.scaleX = scaleFactor
            trainMapIv?.scaleY = scaleFactor
            return true
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showBartMapDialog(event: EventToggleMap) {
        val builder = AlertDialog.Builder(this)
        val view2 = layoutInflater.inflate(R.layout.bartmap_alert_dialog, null)
        trainMapIv = view2.findViewById(R.id.bartMap_custom_imageView)
        trainMapIv?.let {
            //todo: warning for accessibility users
            trainMapIv?.setOnTouchListener { view1, event ->
                scaleGestureDetector?.onTouchEvent(event)
                true
            }

            viewModel.initBartMap(this, trainMapIv)
            builder.setView(view2)
            val dialog = builder.create()
            dialog.show()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CODE_CAMERA = "REQUEST_CODE_CAMERA"
    }
}



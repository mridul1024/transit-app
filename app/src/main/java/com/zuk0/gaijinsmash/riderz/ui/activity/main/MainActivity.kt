package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.zuk0.gaijinsmash.riderz.data.local.entity.event.EventToggleMap
import com.zuk0.gaijinsmash.riderz.databinding.BartmapAlertDialogBinding
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    lateinit var navController: NavController

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : MainViewModel

    private lateinit var weatherPresenter: WeatherPresenter

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        navController = findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(binding.mainBottomNavigation, navController)
        setupWithNavController(binding.navView, navController)
        mAppBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.homeFragment, R.id.googleMapFragment, R.id.tripFragment, R.id.favoritesFragment))
                .setDrawerLayout(binding.drawerLayout)
                .build()

        setupWithNavController(binding.toolbar, navController, mAppBarConfiguration)

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

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onSupportNavigateUp() : Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
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
                //val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                //LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun showBottomNavigation(enable: Boolean) {
        if(enable)
            HideBottomViewOnScrollBehavior<BottomNavigationView>(this, null).slideUp(binding.mainBottomNavigation)
        else
            HideBottomViewOnScrollBehavior<BottomNavigationView>(this, null).slideDown(binding.mainBottomNavigation)
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
            binding.fabOpen.isExpanded = !binding.fabOpen.isExpanded
            animateFAB(binding.fabOpen.isExpanded)
            binding.mainShadowOverlay.visibility = View.VISIBLE
        }
        binding.fabClose.setOnClickListener {
            binding.fabOpen.isExpanded = !binding.fabOpen.isExpanded
            animateFAB(binding.fabOpen.isExpanded)
            binding.mainShadowOverlay.visibility = View.GONE
        }
        binding.fabMap.setOnClickListener {
            showBartMapDialog(EventToggleMap(TAG))
            binding.fabClose.performClick()
        }
        binding.fabComplain.setOnClickListener {
            binding.fabClose.performClick()
            showComplainDialog()
        }
        binding.fabCamera.setOnClickListener {
            binding.fabClose.performClick()
            //deselect bottom nav
            binding.mainBottomNavigation.menu
            showBartPoliceDialog()

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

            lifecycleScope.launch {
                binding.fabCamera.extend()
                binding.fabMap.extend()
                binding.fabComplain.extend()
                delay(2000)
                binding.fabCamera.shrink()
                binding.fabMap.shrink()
                binding.fabComplain.shrink()
            }
        } else {
            binding.fabClose.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_backward)
            binding.fabClose.visibility = View.INVISIBLE
            binding.fabOpen.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_backward)

            binding.fabCamera.visibility = View.INVISIBLE
            binding.fabComplain.visibility = View.INVISIBLE
            binding.fabMap.visibility = View.INVISIBLE
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
        //todo set color of options menu
    }

    private fun initWeather() {
        weatherPresenter = WeatherPresenter(this, viewModel, binding)
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
        val dialogBinding = BartmapAlertDialogBinding.inflate(layoutInflater)
        trainMapIv = dialogBinding.bartMapCustomImageView
        trainMapIv?.setOnTouchListener { _, e ->
            scaleGestureDetector?.onTouchEvent(e)
            true
        }
        viewModel.initBartMap(this, trainMapIv)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()
        dialogBinding.mapCloseFab.setOnClickListener { fab ->
            fab.animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_spin)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showComplainDialog() {
        val dialog = AlertDialog.Builder(this).create()
        dialog.setTitle(getString(R.string.alert_dialog_complain_title))
        dialog.setMessage(getString(R.string.alert_dialog_complain_msg))
        val customView = layoutInflater.inflate(R.layout.wtf_bart_dialog, null, false)
        //set custom view
        dialog.setView(customView)
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_dialog_cancel)) { d, _ ->
            d.cancel()
        }
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_dialog_continue)) { d, _ ->
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","ryanjsuzuki@gmail.com", null)) //TODO remove test email
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "body")
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_report_by_email)))
            d.dismiss()
        }
        dialog.show()
    }

    private fun showBartPoliceDialog() {
        //anavController.navigate(R.id.cameraFragment)
        val dialog = AlertDialog.Builder(this).create()
        //dialog.setTitle(getString(R.string.alert_dialog_report_title))
        //dialog.setMessage(getString(R.string.alert_dialog_report_message))
        val customView = layoutInflater.inflate(R.layout.bart_police_dialog, null, false)
        //set custom view
        dialog.setView(customView)
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_dialog_cancel)) { d, _ ->
            d.cancel()
        }
        dialog.show()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CODE_CAMERA = "REQUEST_CODE_CAMERA"
    }
}



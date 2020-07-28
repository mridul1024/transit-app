package com.zuk0.gaijinsmash.riderz.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector


class SplashActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject lateinit var fragmentInjector : DispatchingAndroidInjector<Any>

    //@Inject lateinit var splashViewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var vm: SplashViewModel

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initDagger()
        super.onCreate(savedInstanceState)
        initViewModel()
        vm.initDayNightTheme()
        initStations()
        launchMainActivity()
    }

    private fun initDagger() {
        AndroidInjection.inject(this)
    }

    private fun initViewModel() {
        vm = ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    //todo: need to move this to application
    private fun initStations() {
        vm.initStationsData()
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

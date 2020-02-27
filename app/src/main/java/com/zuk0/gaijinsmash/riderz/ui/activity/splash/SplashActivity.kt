package com.zuk0.gaijinsmash.riderz.ui.activity.splash

import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
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
    private lateinit var mViewModel: SplashViewModel

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //initDagger()
        super.onCreate(savedInstanceState)
        initViewModel()
        initStations()
        launchMainActivity()
    }

    private fun initDagger() {
        AndroidInjection.inject(this)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    //todo: need to move this to application
    private fun initStations() {
        mViewModel.initStationsData()
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

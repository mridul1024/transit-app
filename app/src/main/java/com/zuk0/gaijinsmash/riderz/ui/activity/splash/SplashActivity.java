package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class SplashActivity extends AppCompatActivity {

    private SplashViewModel mViewModel;

    @Inject
    SplashViewModelFactory splashViewModelFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initDagger();
        super.onCreate(savedInstanceState);
        initViewModel();
        initStations();
        launchMainActivity();
    }

    private void initDagger() {
        AndroidInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, splashViewModelFactory).get(SplashViewModel.class);
    }

    private void initStations() {
        mViewModel.initStationsData();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

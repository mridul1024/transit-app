package com.zuk0.gaijinsmash.riderz.data.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.model.Favorite;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BSA;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.network.BsaCall;
import com.zuk0.gaijinsmash.riderz.data.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesHelper;
import com.zuk0.gaijinsmash.riderz.utils.TimeAndDate;

import java.util.Objects;

import javax.inject.Inject;

/*
    ViewModel classes stores UI data and is lifecycle aware and can handle orientation changes.
    AndroidViewModel is for classes that need to use Context.
 */

public class HomeViewModel extends AndroidViewModel {

    private LiveData<Favorite> favoriteLiveData;
    private LiveData<BSA> bsaLiveData;

    @Inject
    private BsaRepository mBsaRepository;

    //Constructor for AndroidViewModel must have ONLY one parameter, which is Application.
    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    // Only perform initialization once per app lifetime.
    public synchronized void initializeData() {

    }

    public BsaRepository getBsaRepo() {
        return mBsaRepository;
    }

    // Create message for Advisory Time and Date
    public boolean is24HrTimeOn() {
        return SharedPreferencesHelper.getTimePreference(getApplication());
    }

    public String initTime(boolean is24HrTimeOn, LiveData<BsaXmlResponse> data) {
        String time;
        if(is24HrTimeOn) {
            time = Objects.requireNonNull(data.getValue()).getTime();
        } else {
            time = TimeAndDate.convertTo12Hr(Objects.requireNonNull(data.getValue()).getTime());
        }
        return time;
    }

    public String initMessage(String time) {
        return R.string.last_update + " " + time;
    }

    // For the home screen banner picture
    public int getHour() {
        return TimeAndDate.getCurrentHour();
    }

    public void initPic(int hour, ImageView imageView) {
        if(hour < 6 || hour >= 21) {
            // show night picture
            Glide.with(getApplication())
                    .load(R.drawable.sf_night)
                    .into(imageView);
        } else if(hour >= 17) {
            // show dusk picture
            Glide.with(getApplication())
                    .load(R.drawable.sf_dusk)
                    .into(imageView);
        } else {
            Glide.with(getApplication())
                    .load(R.drawable.sf_day)
                    .into(imageView);
        }
    }

} // end of class

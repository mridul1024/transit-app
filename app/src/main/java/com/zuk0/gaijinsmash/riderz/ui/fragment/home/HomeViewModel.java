package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesHelper;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

/*
    ViewModel classes stores UI data and is lifecycle aware and can handle orientation changes.
    AndroidViewModel is for classes that need to use Context.
 */
@Singleton
public class HomeViewModel extends ViewModel {

    //private LiveData<Favorite> favoriteLiveData;
    private LiveData<BsaXmlResponse> bsaLiveData;
    private BsaRepository mBsaRepository;

    //Constructor for AndroidViewModel must have ONLY one parameter, which is Application.
    // Therefore, must create ViewModelProviderFactory to bypass this limitation
    @Inject
    public HomeViewModel(BsaRepository bsaRepository) {
        mBsaRepository = bsaRepository;
        if(mBsaRepository == null) {
            Log.wtf("***HomeViewModel***","repo is null");
        } else {
            Log.wtf("WORKIN MOFO!","YAY");
        }
        initializeData();
    }

    // Only perform initialization once per app lifetime.
    private synchronized void initializeData() {
        if(bsaLiveData == null) {
            bsaLiveData = mBsaRepository.getBsa();
        }
        //todo: init favorites and call
    }

    public BsaRepository getBsaRepo() {
        return mBsaRepository;
    }

    public LiveData<BsaXmlResponse> getBsaLiveData() {
        return bsaLiveData;
    }

    // Create message for Advisory Time and Date
    public boolean is24HrTimeOn(Context context) {
        return SharedPreferencesHelper.getTimePreference(context);
    }

    public String initTime(boolean is24HrTimeOn, LiveData<BsaXmlResponse> data) {
        String time;
        if(is24HrTimeOn) {
            time = Objects.requireNonNull(data.getValue()).getTime();
        } else {
            time = TimeDateUtils.convertTo12Hr(Objects.requireNonNull(data.getValue()).getTime());
        }
        return time;
    }

    public String initMessage(String time) {
        return R.string.last_update + " " + time;
    }

    // For the home screen banner picture
    public int getHour() {
        return TimeDateUtils.getCurrentHour();
    }

    public void initPic(Context context, int hour, ImageView imageView) {
        if(hour < 6 || hour >= 21) {
            // show night picture
            Glide.with(context)
                    .load(R.drawable.sf_night)
                    .into(imageView);
        } else if(hour >= 17) {
            // show dusk picture
            Glide.with(context)
                    .load(R.drawable.sf_dusk)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.sf_day)
                    .into(imageView);
        }
    }

} // end of class

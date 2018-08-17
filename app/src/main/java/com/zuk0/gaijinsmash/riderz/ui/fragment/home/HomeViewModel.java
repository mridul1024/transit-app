package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

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
    HomeViewModel(BsaRepository bsaRepository) {
        mBsaRepository = bsaRepository;
        initData();
    }

    // Only perform initialization once per app lifetime.
    private synchronized void initData() {
        if(bsaLiveData == null) {
            bsaLiveData = mBsaRepository.getBsa();
        }
        //todo: init favorites and call
    }

    public LiveData<BsaXmlResponse> getBsaLiveData() {
        return bsaLiveData;
    }

    // Create message for Advisory Time and Date
    public boolean is24HrTimeOn(Context context) {
        return SharedPreferencesUtils.getTimePreference(context);
    }

    private String initTime(boolean is24HrTimeOn, String time) {
        String result;
        if(is24HrTimeOn) {
            result = TimeDateUtils.format24hrTime(time);
        } else {
            result = TimeDateUtils.convertTo12Hr(time);
        }
        return result;
    }

    public String initMessage(Context context, boolean is24HrTimeOn, String time) {
        return context.getResources().getString(R.string.last_update) + " " + initTime(is24HrTimeOn, time);
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

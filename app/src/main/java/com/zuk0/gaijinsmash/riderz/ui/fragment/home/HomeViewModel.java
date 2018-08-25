package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.BsaRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.EtdRepository;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/*
    ViewModel classes stores UI data and is lifecycle aware and can handle orientation changes.
    AndroidViewModel is for classes that need to use Context.
 */
@Singleton
public class HomeViewModel extends ViewModel {

    private LiveData<List<Favorite>> mFavoritesLiveData;
    private LiveData<EtdXmlResponse> mEtdLiveData;
    private EtdRepository mEtdRepository;

    private LiveData<BsaXmlResponse> mBsaLiveData;
    private BsaRepository mBsaRepository;

    //Constructor for AndroidViewModel must have ONLY one parameter, which is Application.
    // Therefore, must create ViewModelProviderFactory to bypass this limitation
    @Inject
    HomeViewModel(BsaRepository bsaRepository, EtdRepository etdRepository) {
        mBsaRepository = bsaRepository;

        initData();
    }

    // Only perform initialization once per app lifetime.
    private synchronized void initData() {
        if(mBsaLiveData == null) {
            mBsaLiveData = mBsaRepository.getBsa();
        }

        //todo: init favorites and call
    }

    public LiveData<BsaXmlResponse> getBsaLiveData() {
        return mBsaLiveData;
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

    // todo: get favorites, get priority favorite - top 2
    private LiveData<List<Favorite>> getFavoritesLiveData(Context context) {
        if(mFavoritesLiveData == null) {
            FavoriteDatabase.getRoomDB(context).getFavoriteDAO().getAllPriorityFavorites();
        }
        return mFavoritesLiveData;
    }

    // todo: make call with repo
    public LiveData<EtdXmlResponse> getEtdLiveData(Context context) {
        if(mEtdLiveData == null) {
            getFavoritesLiveData(context).observe((LifecycleOwner) this, favorites -> {
                if (favorites != null) {
                    Log.i("HomeViewModel", "fetching fav Live Data");
                    for(Favorite favorite : favorites) {
                        mEtdLiveData = mEtdRepository.getEtd(favorite.getOrigin());
                        //todo: how to combine results of both
                        //getEtdLiveData(favorite.getDestination());
                    }
                }
            });
        }
        return mEtdLiveData;
    }

    //todo: to display the remaining time before a departure on ETD
    public CountDownTimer initTimer(long milliUntilFinished, long countDownInterval) {
       return new CountDownTimer(milliUntilFinished, countDownInterval) {
           @Override
           public void onTick(long millisUntilFinished) {
               //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
           }

           @Override
           public void onFinish() {
                //mTextField.setText("done!");
           }
       };
    }
} // end of class

package com.zuk0.gaijinsmash.riderz.ui.fragment.trip;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import javax.inject.Singleton;

@Singleton
public class TripViewModel extends ViewModel {

    public String getTimeForTripSearch(String preformatTime, boolean is24HrTimeOn) {
        String result;
        if (!preformatTime.equals("Now") && is24HrTimeOn) {
            String convertedTime = TimeDateUtils.convertTo12HrForTrip(preformatTime);
            result = TimeDateUtils.formatTime(convertedTime);
        } else if (!preformatTime.equals("Now") && !is24HrTimeOn) {
            result = TimeDateUtils.formatTime(preformatTime); // time=h:mm+AM/PM
        } else {
            result = preformatTime;
        }
        return result;
    }
}

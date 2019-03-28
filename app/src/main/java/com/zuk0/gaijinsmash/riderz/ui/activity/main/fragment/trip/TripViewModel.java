package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip;

import androidx.lifecycle.ViewModel;

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

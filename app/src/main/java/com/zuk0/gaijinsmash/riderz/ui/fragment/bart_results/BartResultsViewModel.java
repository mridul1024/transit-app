package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BartResultsViewModel extends ViewModel {

    private LiveData<TripXmlResponse> mTrip;
    private TripRepository mTripRepository;

    @Inject
    BartResultsViewModel(TripRepository tripRepository) {
        mTripRepository = tripRepository;
    }

    public LiveData<TripXmlResponse> getTripLiveData() {
        return mTrip;
    }

    public LiveData<TripXmlResponse> getTrip(String origin, String destination, String date, String time) {
        if(mTrip == null) {
            mTrip = mTripRepository.getTrip(origin, destination, date, time, 0, 4);
        }
        //todo: convert stations to abbr
        return mTrip;
    }

    public LiveData<List<Station>> getStationsFromDb(Context context, String origin, String destination) {
        return StationDatabase.getRoomDB(context).getStationDAO().getOriginAndDestination(origin, destination);
    }

    public void initFavoritesIcon() {

    }

    private void loadData() {
    }


}

package com.zuk0.gaijinsmash.riderz.ui.fragment.station_info;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;

import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.repository.StationRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class StationInfoViewModel extends ViewModel {

    private StationRepository mRepository;
    private LiveData<StationXmlResponse> mStationLiveData;

    @Inject
    StationInfoViewModel(StationRepository repository) {
        mRepository = repository;
        initData();
    }

    private synchronized void initData() {
        //todo: initialization logic here
    }

    public LiveData<StationXmlResponse> getStationLiveData() {
        return mStationLiveData;
    }

    enum StationInfo {
        Name("StationName"), Lat("StationLat"), Long("StationLong");

        private String value;

        StationInfo(String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }

    public Bundle getBundle(Station station) {
        Bundle bundle = new Bundle();
        bundle.putString(StationInfo.Name.getValue(), station.getName());
        bundle.putString(StationInfo.Lat.getValue(), String.valueOf(station.getLatitude()));
        bundle.putString(StationInfo.Long.getValue(), String.valueOf(station.getLongitude()));
        return bundle;
    }


    // NOTE: this must be called first before trying to fetch LiveData object
    public void initStation(Context context, String abbr) {
        if(mStationLiveData == null) {
            mStationLiveData = mRepository.getStation(abbr);
        }
    }
}

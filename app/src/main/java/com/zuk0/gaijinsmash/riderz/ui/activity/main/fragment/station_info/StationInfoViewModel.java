package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StationInfoViewModel extends ViewModel {

    private StationRepository mRepository;
    private LiveData<StationXmlResponse> mStationLiveData;

    public String mStationAbbr;
    public Station mStationObject;
    public Bundle mBundle;

    @Inject
    StationInfoViewModel(StationRepository repository) {
        mRepository = repository;
        initData();
    }

    private synchronized void initData() {
        //todo: initialization logic here
    }

    public LiveData<StationXmlResponse> getStationLiveData(String abbr) {
        if(mStationLiveData == null) {
            mStationLiveData = mRepository.getStation(abbr);
        }
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
}

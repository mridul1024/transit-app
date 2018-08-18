package com.zuk0.gaijinsmash.riderz.ui.fragment.stations;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.repository.StationRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StationsViewModel extends ViewModel {

    private StationRepository mRepository;
    private LiveData<StationXmlResponse> mListFromRepo;

    @Inject
    StationsViewModel(StationRepository repository) {
        this.mRepository = repository;
    }

    public LiveData<List<Station>> getListFromDb(Context context) {
        return StationDatabase.getRoomDB(context).getStationDAO().getStationsLiveData();
    }

    public LiveData<StationXmlResponse> getListFromRepo() {
        if(mListFromRepo == null) {
            mListFromRepo = mRepository.getStations();
        }
        return mListFromRepo;
    }
}

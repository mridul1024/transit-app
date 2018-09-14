package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class SplashViewModel extends AndroidViewModel {

    private static final int BART_STATIONS_COUNT = 48;

    private StationRepository mRepository;

    @Inject
    SplashViewModel(Application application, StationRepository repository) {
        super(application);
        mRepository = repository;
    }

    // todo: first check database - if database is null or too low, then
    public void loadStations() {
        new LoadStationsTask(super.getApplication(), mRepository).execute();
    }

    private static class LoadStationsTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Application> mWeakRef;

        StationRepository mRepository;

        LoadStationsTask(Application application, StationRepository repository) {
            mWeakRef = new WeakReference<>(application);
            mRepository = repository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Application ref = mWeakRef.get();
            int count = StationDatabase.getRoomDB(mWeakRef.get())
                    .getStationDAO()
                    .countStations();

            if(count < BART_STATIONS_COUNT) {
                // make api call and all stations to RoomDb
                List<Station> stationList = Objects.requireNonNull(mRepository
                        .getStations()
                        .getValue())
                        .getStationList();

                for(Station station : stationList) {
                    Log.i("station added", station.getName());
                    StationDatabase.getRoomDB(mWeakRef.get()).getStationDAO().save(station);
                }
            }
            return null;
        }
    }
}

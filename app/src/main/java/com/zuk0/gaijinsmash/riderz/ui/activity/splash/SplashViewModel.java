package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.helper.StationDbHelper;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class SplashViewModel extends AndroidViewModel {

    private static final int BART_STATIONS_COUNT = 48;

    private StationRepository mRepository;

    private LiveData<StationXmlResponse> mStationsLiveData;

    @Inject
    SplashViewModel(Application application, StationRepository repository) {
        super(application);
        mRepository = repository;
        initStationsDb();
    }

    private void initStationsDb() {
        if(mStationsLiveData == null) {
            mStationsLiveData = mRepository.getStations();
        }
    }

    public LiveData<StationXmlResponse> getStations() {
        return mStationsLiveData;
    }

    public void loadStations() {
        new LoadStationsTask(super.getApplication()).execute();
    }

    private static class LoadStationsTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Application> mWeakRef;

        LoadStationsTask(Application application) {
            mWeakRef = new WeakReference<>(application);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Application ref = mWeakRef.get();
            StationDbHelper helper = new StationDbHelper(ref.getApplicationContext());
            try {
                helper.initStationDb(ref.getApplicationContext());
            } catch (IOException | XmlPullParserException e) {
                Log.e("Splash Activity", e.getMessage());
            } finally {
                helper.closeDb();
            }
            return null;
        }
    }
}

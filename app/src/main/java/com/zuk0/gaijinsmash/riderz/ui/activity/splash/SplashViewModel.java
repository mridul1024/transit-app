package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.StationRepository;
import com.zuk0.gaijinsmash.riderz.utils.xml_parser.StationXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

public class SplashViewModel extends AndroidViewModel {

    private static final int BART_STATIONS_COUNT = 48;

    @Inject
    SplashViewModel(Application application) {
        super(application);
    }

    public void initStationsData() {
        new SaveStationsTask(super.getApplication()).execute();
    }

    private static class SaveStationsTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Application> mWeakRef;

        SaveStationsTask(Application application) {
            mWeakRef = new WeakReference<>(application);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int count = getCount();
            if(count < BART_STATIONS_COUNT) {
                List<Station> stationList = getList();
                saveList(stationList);
            }
            return null;
        }

        private int getCount() {
            int count = StationDatabase.getRoomDB(mWeakRef.get())
                    .getStationDAO()
                    .countStations();
            Log.i("doInBackground", String.valueOf(count));
            return count;
        }

        private List<Station> getList() {
            InputStream is;
            List<Station> stationList = null;
            try {
                is = mWeakRef.get().getAssets().open("stations.xml");
                StationXmlParser parser = new StationXmlParser(is);
                stationList = parser.getList();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return stationList;
        }

        private void saveList(List<Station> stationList) {
            if(stationList != null) {
                for(Station station : stationList) {
                    Log.i("station added", station.getName());
                    StationDatabase.getRoomDB(mWeakRef.get()).getStationDAO().save(station);
                }
            }
        }
    }
}

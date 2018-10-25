package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitInterface;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class StationRepository  {

    private final RetrofitInterface service;
    private final StationDao stationDAO;
    private final Executor executor;

    @Inject
    StationRepository(RetrofitInterface service, StationDao stationDAO, Executor executor) {
        this.service = service;
        this.stationDAO = stationDAO;
        this.executor = executor;
    }

    public LiveData<StationXmlResponse> getStation(String abbr) {
        //todo: if cached != null, return cached

        final MutableLiveData<StationXmlResponse> data = new MutableLiveData<>();
        service.getStation(abbr).enqueue(new Callback<StationXmlResponse>() {
            @Override
            public void onResponse(@NonNull Call<StationXmlResponse> call, @NonNull Response<StationXmlResponse> response) {
                data.postValue(response.body());
                Log.i("Station", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<StationXmlResponse> call, @NonNull Throwable t) {
                Log.wtf("StationRepository", t.getMessage());
            }
        });
        return data;
    }

    public LiveData<StationXmlResponse> getStations() {
        final MutableLiveData<StationXmlResponse> data = new MutableLiveData<>();
        service.getAllStations().enqueue(new Callback<StationXmlResponse>() {
            @Override
            public void onResponse(@NonNull Call<StationXmlResponse> call, @NonNull Response<StationXmlResponse> response) {
                data.postValue(response.body());
                Log.i("Stations", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<StationXmlResponse> call, @NonNull Throwable t) {
                Log.wtf("StationRepository", t.getMessage());
            }
        });
        return data;
    }

    public void refreshStation(final String name) {
        executor.execute(() -> {
          boolean stationExists = stationDAO.getStationByName(name) != null;
          if(!stationExists) {
              // refresh data
              try {
                  Response response = service.getStation(name).execute();
                  stationDAO.save((Station) response.body());
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
        });
    }
}

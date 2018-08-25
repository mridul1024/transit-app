package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.dao.TripDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitInterface;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class TripRepository {

    private final RetrofitInterface service;
    private final TripDao tripDao;
    private final Executor executor;

    @Inject
    TripRepository(RetrofitInterface service, TripDao tripDao, Executor executor) {
        this.service = service;
        this.tripDao = tripDao;
        this.executor = executor;
    }

    public LiveData<TripXmlResponse> getTrip(String origin, String destination, String date, String time) {
        final MutableLiveData<TripXmlResponse> data = new MutableLiveData<>();
        service.getTrip(origin, destination, date, time).enqueue(new Callback<TripXmlResponse>() {
            @Override
            public void onResponse(Call<TripXmlResponse> call, Response<TripXmlResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(Call<TripXmlResponse> call, Throwable t) {
                Log.wtf("Trip", t.getMessage());
            }
        });
        return data;
    }

    public void refreshTrip(final int tripId) {
        executor.execute(() -> {
            //todo  save data logic here
        });
    }
}

package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.dao.TripDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
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
    private final Executor executor;

    @Inject
    TripRepository(RetrofitInterface service, TripDao tripDao, Executor executor) {
        this.service = service;
        TripDao tripDao1 = tripDao;
        this.executor = executor;
    }

    public LiveData<TripJsonResponse> getTrip(String origin, String destination, String date, String time) {
        final MutableLiveData<TripJsonResponse> data = new MutableLiveData<>();
        service.getTripJson(origin, destination, date, time).enqueue(new Callback<TripJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<TripJsonResponse> call, @NonNull Response<TripJsonResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TripJsonResponse> call, @NonNull Throwable t) {
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

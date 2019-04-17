package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService;
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class TripRepository {

    private final BartService service;
    private final Executor executor;
    private final TripDao tripDao;

    @Inject
    TripRepository(BartService service, TripDao tripDao, Executor executor) {
        this.service = service;
        this.tripDao = tripDao;
        this.executor = executor;
    }

    public LiveData<LiveDataWrapper<TripJsonResponse>> getTrip(String origin, String destination, String date, String time) {
        //todo: if cached != null, return cached
        final MutableLiveData<LiveDataWrapper<TripJsonResponse>> data = new MutableLiveData<>();

        service.getTripJson(origin, destination, date, time).enqueue(new Callback<TripJsonResponse>() {

            @Override
            public void onResponse(@NonNull Call<TripJsonResponse> call, @NonNull Response<TripJsonResponse> response) {
                Log.i("onResponse", response.body().toString());
                LiveDataWrapper<TripJsonResponse> res = LiveDataWrapper.success(response.body());
                data.postValue(res);

                if(response.code() == 502) {
                    //try again
                    // todo; bad gateway request handle, possibly interceptor?
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripJsonResponse> call, @NonNull Throwable t) {
                Log.wtf("onFailure", "trip: " + t.getMessage());
                LiveDataWrapper<TripJsonResponse> res = LiveDataWrapper.error(null, t.getMessage());
                data.setValue(res);
            }
        });
        return data;
    }

    public void refreshTrip() {

        // runs in a background thread
        executor.execute(() -> {
            //todo  save data logic here
            //boolean tripExists = tripDao.hasTrip();
        });
    }
}

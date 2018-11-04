package com.zuk0.gaijinsmash.riderz.data.local.entity;

import android.arch.lifecycle.LiveData;

import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;

import java.util.List;

public class MergedTrip {

    private LiveData<TripJsonResponse> trips1;
    private LiveData<TripJsonResponse> trips2;

    public LiveData<TripJsonResponse> getTrips1() {
        return trips1;
    }

    public void setTrips1(LiveData<TripJsonResponse> trips1) {
        this.trips1 = trips1;
    }

    public LiveData<TripJsonResponse> getTrips2() {
        return trips2;
    }

    public void setTrips2(LiveData<TripJsonResponse> trips2) {
        this.trips2 = trips2;
    }
}

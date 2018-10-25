package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitInterface;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    This repo abstracts the data sources from the rest of the app. ViewModel doesn't know
    that the data is fetched by the RetrofitInterface. Repository makes API calls and fetches data.
    https://proandroiddev.com/the-missing-google-sample-of-android-architecture-components-guide-c7d6e7306b8f
*/

@Singleton
public class BsaRepository {

    private final RetrofitInterface service;
    private final BsaDao bsaDao;
    private final Executor executor;

    @Inject
    BsaRepository(RetrofitInterface service, BsaDao bsaDao, Executor executor) {
        this.service = service;
        this.bsaDao = bsaDao; // for caching
        this.executor = executor; // for executing on new thread
    }

    public LiveData<BsaXmlResponse> getBsa() {
        //todo: if cached != null, return cached

        final MutableLiveData<BsaXmlResponse> data = new MutableLiveData<>();
        service.getBsa().enqueue(new Callback<BsaXmlResponse>() {
            @Override
            public void onResponse(@NonNull Call<BsaXmlResponse> call, @NonNull Response<BsaXmlResponse> response) {
                data.postValue(response.body());
                Log.i("bsa repository", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<BsaXmlResponse> call, @NonNull Throwable t) {
                Log.wtf("*****BsaRepository******", t.getMessage());
            }
        });
        return data;
    }

    public void refreshBsa(final int bsaId) {
        executor.execute(() -> {
            // running in background thread
            // check if bsa was fetched recently
            boolean bsaExists = bsaDao.bsaExists(bsaId);
            if(!bsaExists) {
                // refresh the data
                try {
                    Response response = service.getBsa().execute();
                    //todo: check for error
                    // update the database, livedata auto refreshes
                    bsaDao.save((BsaXmlResponse) response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

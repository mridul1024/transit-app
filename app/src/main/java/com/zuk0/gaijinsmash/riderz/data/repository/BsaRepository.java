package com.zuk0.gaijinsmash.riderz.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.database.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.network.retrofit.RetrofitService;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    This repo abstracts the data sources from the rest of the app. ViewModel doesn't know
    that the data is fetched by the RetrofitService. Repository makes API calls and fetches data.
 */

@Singleton // informs Dagger that this class should be constructed once
public class BsaRepository {
    private final RetrofitService retrofitService;
    private final BsaDao bsaDao; // cache
    private final Executor executor;

    @Inject
    public BsaRepository(RetrofitService retrofitService, BsaDao bsaDao, Executor executor) {
        this.retrofitService = retrofitService;
        this.bsaDao = bsaDao;
        this.executor = executor;
    }

    public LiveData<BsaXmlResponse> getAdvisory() {
        final MutableLiveData<BsaXmlResponse> data = new MutableLiveData<>();
        retrofitService.getBsa().enqueue(new Callback<BsaXmlResponse>() {
            @Override
            public void onResponse(@NonNull Call<BsaXmlResponse> call, @NonNull Response<BsaXmlResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<BsaXmlResponse> call, @NonNull Throwable t) {
                Log.wtf("BsaRepository", t.getMessage());
            }
        });
        return data;
    }

    private void refreshBsa(final int bsaId) {
        executor.execute(() -> {
            // running in background thread
            // check if bsa was fetched recently
            boolean bsaExists = bsaDao.bsaExists(bsaId);
            if(!bsaExists) {
                // refresh the data
                try {
                    Response response = retrofitService.getBsa().execute();
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

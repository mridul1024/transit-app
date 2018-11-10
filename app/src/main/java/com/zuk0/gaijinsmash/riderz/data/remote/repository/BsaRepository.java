package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    This repo abstracts the data sources from the rest of the app. ViewModel doesn't know
    that the data is fetched by the RetrofitService. Repository makes API calls and fetches data.
    https://proandroiddev.com/the-missing-google-sample-of-android-architecture-components-guide-c7d6e7306b8f
*/

@Singleton
public class BsaRepository {

    private final RetrofitService service;
    private final BsaDao bsaDao;
    private final Executor executor;

    @Inject
    public BsaRepository(RetrofitService service, BsaDao bsaDao, Executor executor) {
        this.service = service;
        this.bsaDao = bsaDao; // for caching
        this.executor = executor; // for executing on new thread
    }

    public LiveData<BsaXmlResponse> getBsa() {
        refreshBsa(new Timestamp(System.currentTimeMillis()));
        return bsaDao.load();
    }

    //todo: add test
    private void refreshBsa(final Timestamp current) {
        // running in background thread
        executor.execute(() -> {
            // No more than 5 minutes in the past
            Long min = current.getTime() - 60000 * 5;
            boolean bsaExists = bsaDao.bsaExists(new Timestamp(min),current);
            if(!bsaExists) {
                try {
                    Response response = service.getBsa().execute();
                    BsaXmlResponse bsa = (BsaXmlResponse) response.body();
                    bsa.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    Log.d("refreshBsa()", bsa.getDate() + " | " + bsa.getBsaList().get(0).getDescription());
                    bsaDao.save(bsa);
                } catch (IOException e) {
                    Log.wtf("refreshBsa()", e.getMessage());
                }
            }
        });
    }
}

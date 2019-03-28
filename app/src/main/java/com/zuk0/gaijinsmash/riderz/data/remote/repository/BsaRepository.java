package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.MutableLiveData;
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
        //refreshBsa(new Timestamp(System.currentTimeMillis()));

        MutableLiveData<BsaXmlResponse> data = new MutableLiveData<>();
        service.getBsa().enqueue(new Callback<BsaXmlResponse>() {
            @Override
            public void onResponse(@NotNull Call<BsaXmlResponse> call, @NotNull Response<BsaXmlResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<BsaXmlResponse> call, @NotNull Throwable t) {
                Log.e("onFailure", "bsa: " + t.getMessage());
            }
        });
        return data;
    }

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
                    if (bsa != null) {
                        bsa.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    }
                    bsaDao.save(bsa);
                } catch (IOException e) {
                    Log.wtf("refreshBsa()", e.getMessage());
                }
            }
        });
    }
}

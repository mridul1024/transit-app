package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class EtdRepository {

    private final BartService service;
    private Executor executor;
    private EtdDao etdDao;

    @Inject
    EtdRepository(BartService service, EtdDao etdDao, Executor executor) {
        this.service = service;
        this.etdDao = etdDao;
        this.executor = executor;
    }

    public LiveData<EtdXmlResponse> getEtd(String originAbbr) {
        //todo: if cached != null, return cached

        final MutableLiveData<EtdXmlResponse> data = new MutableLiveData<>();
        service.getEtd(originAbbr).enqueue(new Callback<EtdXmlResponse>() {
            @Override
            public void onResponse(@NonNull Call<EtdXmlResponse> call, @NonNull Response<EtdXmlResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<EtdXmlResponse> call, @NonNull Throwable t) {
                Log.wtf("EtdRepository", t.getMessage());
            }
        });
        return data;
    }

    public void refreshEtd(String origin) {
        //todo: insert logic
    }
}

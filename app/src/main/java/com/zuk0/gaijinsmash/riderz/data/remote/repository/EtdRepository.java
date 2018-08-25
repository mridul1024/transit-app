package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitInterface;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Estimates
public class EtdRepository {

    private final RetrofitInterface service;
    private final EtdDao etdDao;
    private final Executor executor;

    @Inject
    EtdRepository(RetrofitInterface service, EtdDao etdDao, Executor executor) {
        this.service = service;
        this.etdDao = etdDao;
        this.executor = executor;
    }

    public LiveData<EtdXmlResponse> getEtd(String originAbbr) {
        final MutableLiveData<EtdXmlResponse> data = new MutableLiveData<>();
        service.getEtd(originAbbr).enqueue(new Callback<EtdXmlResponse>() {
            @Override
            public void onResponse(Call<EtdXmlResponse> call, Response<EtdXmlResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(Call<EtdXmlResponse> call, Throwable t) {
                Log.wtf("EtdRepository", t.getMessage());
            }
        });
        return null;
    }

    public void refreshEtd() {
        //todo: insert logic
    }
}

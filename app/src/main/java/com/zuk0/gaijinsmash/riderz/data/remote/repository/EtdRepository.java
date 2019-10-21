package com.zuk0.gaijinsmash.riderz.data.remote.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.BartService;
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
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

    public LiveData<LiveDataWrapper<EtdXmlResponse>> getEtd(String originAbbr) {
        //todo: if cached != null, return cached

        final MutableLiveData<LiveDataWrapper<EtdXmlResponse>> data = new MutableLiveData<>();
        service.getEtd(originAbbr).enqueue(new Callback<EtdXmlResponse>() {
            @Override
            public void onResponse(@NonNull Call<EtdXmlResponse> call, @NonNull Response<EtdXmlResponse> response) {
                Logger.i(response.message());
                LiveDataWrapper<EtdXmlResponse> res = LiveDataWrapper.success(response.body());
                data.postValue(res);
            }

            @Override
            public void onFailure(@NonNull Call<EtdXmlResponse> call, @NonNull Throwable t) {
                Log.wtf("EtdRepository", t.getMessage());
                LiveDataWrapper<EtdXmlResponse> res = LiveDataWrapper.error(null, t.getLocalizedMessage());
                data.postValue(res);
            }
        });
        return data;
    }

    public void refreshEtd(String origin) {
        //todo: insert logic
    }
}

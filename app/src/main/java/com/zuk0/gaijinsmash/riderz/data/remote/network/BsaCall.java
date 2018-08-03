package com.zuk0.gaijinsmash.riderz.data.remote.network;

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.ApiUtils;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitInterface;

import io.reactivex.Single;

public class BsaCall {

    private static final String TAG = "BSA Call";
    private RetrofitInterface mService;

    public BsaCall() { mService = ApiUtils.getBartApiService(); }

    public Single<BsaXmlResponse> getBsaSingleObservable() {
        return mService.getBsaRx();
    }

}

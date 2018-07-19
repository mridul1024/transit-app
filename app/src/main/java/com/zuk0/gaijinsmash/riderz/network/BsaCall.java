package com.zuk0.gaijinsmash.riderz.network;

import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.network.retrofit.ApiUtils;
import com.zuk0.gaijinsmash.riderz.network.retrofit.RetrofitService;

import io.reactivex.Single;

public class BsaCall {

    private static final String TAG = "BSA Call";
    private RetrofitService mService;

    public BsaCall() { mService = ApiUtils.getBartApiService(); }

    public Single<BsaXmlResponse> getBsaSingleObservable() {
        return mService.getBsaRx();
    }

}

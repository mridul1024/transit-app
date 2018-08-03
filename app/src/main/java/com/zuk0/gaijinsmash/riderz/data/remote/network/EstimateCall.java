package com.zuk0.gaijinsmash.riderz.data.remote.network;

import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.ApiUtils;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.RetrofitInterface;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EstimateCall {

    private RetrofitInterface mService;
    private static final String TAG = "Estimate Call";

    public EstimateCall() {
        mService = ApiUtils.getBartApiService();
    }

    // STEP 1: CREATE OBSERVABLE - Single is used when the Observable has to emit only one value, like a response from a network call
    public Single<EtdXmlResponse> getEtdSingleObservable(String originAbbr) {
        return mService.getEtdRx(originAbbr);
    }

    //STEP 2: HANDLE OBSERVABLE - scheudlers help
    public void handleObservable(Single<EtdXmlResponse> single) {
        single
                .subscribeOn(Schedulers.io()) //perform operation of Observable on different thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getEtdSingleObserver()); // used to bring back the execution to the main thread to modify UI
    }

    // STEP 3: CREATE OBSERVER
    public SingleObserver<EtdXmlResponse> getEtdSingleObserver() {
        return new SingleObserver<EtdXmlResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onSuccess(EtdXmlResponse etdXmlResponse) {
                Log.d(TAG, " onNext value : " + etdXmlResponse.toString());
                // handle views here

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, " onError: " + e.getMessage());
            }
        };
    }
}

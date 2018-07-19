package com.zuk0.gaijinsmash.riderz.network.retrofit;

import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.model.etd_response.EtdXmlResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("etd.aspx?cmd=etd")
    Call<EtdXmlResponse> getEtd(@Query("orig") String origin);

    @GET("etd.aspx?cmd=etd")
    Single<EtdXmlResponse> getEtdRx(@Query("orig") String origin);

    @GET("bsa.aspx?cmd=bsa")
    Single<BsaXmlResponse> getBsaRx();

    @GET("bsa.aspx?cmd=bsa")
    Call<BsaXmlResponse> getBsa();


}

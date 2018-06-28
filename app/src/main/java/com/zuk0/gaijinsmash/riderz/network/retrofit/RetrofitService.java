package com.zuk0.gaijinsmash.riderz.network.retrofit;

import com.zuk0.gaijinsmash.riderz.model.bart.Station;
import com.zuk0.gaijinsmash.riderz.model.bart.etd_response.EtdXmlResponse;

import org.xmlpull.v1.builder.XmlDocument;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("etd.aspx?cmd=etd")
    Call<EtdXmlResponse> getEtd(@Query("orig") String origin);

    @GET("etd.aspx?cmd=etd")
    Single<EtdXmlResponse> getEtdRx(@Query("orig") String origin);
}

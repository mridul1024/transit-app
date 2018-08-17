package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;

import javax.inject.Singleton;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Singleton
public interface RetrofitInterface {

    @GET("etd.aspx?cmd=etd")
    Call<EtdXmlResponse> getEtd(@Query("orig") String origin);

    @GET("etd.aspx?cmd=etd")
    Single<EtdXmlResponse> getEtdRx(@Query("orig") String origin);

    @GET("bsa.aspx?cmd=bsa")
    Single<BsaXmlResponse> getBsaRx();

    @GET("bsa.aspx?cmd=bsa")
    Call<BsaXmlResponse> getBsa();

    //todo: test this
    @GET("stn.aspx?cmd=stns")
    Call<StationXmlResponse> getAllStations();

    //todo: test this
    @GET("stn.aspx?cmd=stninfo")
    Call<StationXmlResponse> getStation(@Query("orig") String originAbbr);

}

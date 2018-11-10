package com.zuk0.gaijinsmash.riderz.data.remote.retrofit;

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.JsonAndXmlConverters.Json;
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.JsonAndXmlConverters.Xml;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Singleton
public interface RetrofitService {

    @Json
    @GET("sched.aspx?cmd=depart&b=0&a=4&json=y")
    Call<TripJsonResponse> getTripJson(
            @Query("orig") String origin,
            @Query("dest") String destination,
            @Query("date") String date,
            @Query("time") String time);

    @Xml
    @GET("etd.aspx?cmd=etd")
    Call<EtdXmlResponse> getEtd(@Query("orig") String origin);

    @Xml
    @GET("bsa.aspx?cmd=bsa")
    Call<BsaXmlResponse> getBsa();

    //todo: test this
    @Xml
    @GET("stn.aspx?cmd=stns")
    Call<StationXmlResponse> getAllStations();

    //todo: test this
    @Xml
    @GET("stn.aspx?cmd=stninfo")
    Call<StationXmlResponse> getStation(@Query("orig") String originAbbr);
}

package com.zuk0.gaijinsmash.riderz.data.remote.retrofit

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaJsonResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.StationXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.ResponseTypeConverter.Json
import com.zuk0.gaijinsmash.riderz.data.remote.retrofit.ResponseTypeConverter.Xml
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BartService {

    @Json
    @GET("sched.aspx?cmd=depart&b=0&a=4&json=y")
    fun getTripJson(
            @Query("orig") origin: String?,
            @Query("dest") destination: String?,
            @Query("date") date: String?,
            @Query("time") time: String?): Call<TripJsonResponse>

    @Xml
    @GET("etd.aspx?cmd=etd")
    fun getEtd(@Query("orig") origin: String?): Call<EtdXmlResponse>

    @Xml
    @GET("bsa.aspx?cmd=bsa")
    fun getBsa(): Call<BsaXmlResponse>

    @Json
    @GET("bsa.aspx?cmd=bsa&json=y")
    fun getBsaJson(): Call<BsaJsonResponse>

    //todo: test this
    @Xml
    @GET("stn.aspx?cmd=stns")
    fun  getAllStations(): Call<StationXmlResponse>

    //todo: test this
    @Xml
    @GET("stn.aspx?cmd=stninfo")
    fun getStation(@Query("orig") originAbbr: String): Call<StationXmlResponse>
}
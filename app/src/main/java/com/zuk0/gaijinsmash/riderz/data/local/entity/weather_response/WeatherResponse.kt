package com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WeatherResponse(

        @Expose
        @SerializedName("id")
        var id: Int? = null,

        @Expose
        @SerializedName("name")
        var name: String? = null,

        @Expose
        @SerializedName("cod")
        var cod: Int? = null,

        @Expose
        @SerializedName("coord")
        var coordinates: Coordinates? = null,

        @Expose
        @SerializedName("weather")
        var weather: Weather? = null,

        @Expose
        @SerializedName("base")
        var base: String? = null,

        @Expose
        @SerializedName("main")
        var main: Main? = null,

        @Expose
        @SerializedName("sys")
        var sys: Sys? = null,

        @Expose
        @SerializedName("dt")
        var dt: Long? = null,

        @Expose
        @SerializedName("clouds")
        var clouds: Clouds? = null,

        @Expose
        @SerializedName("visibility")
        var visibility: Int? = null,

        @Expose
        @SerializedName("wind")
        var wind: Wind? = null

)
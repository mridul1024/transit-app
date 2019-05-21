package com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//todo is an array of objects
data class Weather(
        @Expose
        @SerializedName("id")
        var id: Int? = null,

        @Expose
        @SerializedName("main")
        var main: String? = null,

        @Expose
        @SerializedName("description")
        var description: String? = null,

        @Expose
        @SerializedName("icon")
        var icon: String? = null
)
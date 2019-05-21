package com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coordinates(
        @Expose
        @SerializedName("lon")
        var longitude: Double,

        @Expose
        @SerializedName("lat")
        var latitude: Double
)
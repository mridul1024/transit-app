package com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Wind (
        @Expose
        @SerializedName("speed")
        var speed: Double? = null,

        @Expose
        @SerializedName("deg")
        var deg: Double? = null
)
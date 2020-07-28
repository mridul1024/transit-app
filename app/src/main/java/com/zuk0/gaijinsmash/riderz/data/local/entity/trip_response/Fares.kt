package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Fares {
    @SerializedName("@level")
    @Expose
    var level: String? = null
    @SerializedName("fare")
    @Expose
    var fare: List<Fare>? = null

}
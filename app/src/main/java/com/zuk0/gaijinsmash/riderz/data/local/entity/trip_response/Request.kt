package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Request {
    @SerializedName("trip")
    @Expose
    var tripList: List<Trip>? = null

}
package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Schedule {
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("before")
    @Expose
    var before: String? = null
    @SerializedName("after")
    @Expose
    var after: String? = null
    @SerializedName("request")
    @Expose
    var request: Request? = null

}
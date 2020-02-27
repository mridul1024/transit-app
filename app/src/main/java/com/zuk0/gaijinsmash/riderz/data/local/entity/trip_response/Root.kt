package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Root {
    @SerializedName("@id")
    @Expose
    var id: String? = null
    @SerializedName("uri")
    @Expose
    var uri: com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Uri? = null
    @SerializedName("origin")
    @Expose
    var origin: String? = null
    @SerializedName("destination")
    @Expose
    var destination: String? = null
    @SerializedName("sched_num")
    @Expose
    var schedNum: String? = null
    @SerializedName("schedule")
    @Expose
    var schedule: com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Schedule? = null
    @SerializedName("message")
    @Expose
    var message = ""

}
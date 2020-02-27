package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ryanj on 12/1/2017.
 */
class Leg {
    @SerializedName("@order")
    @Expose
    var order: String? = null
    @SerializedName("@origin")
    @Expose
    var origin: String? = null
    @SerializedName("@destination")
    @Expose
    var destination: String? = null
    @SerializedName("@origTimeMin")
    @Expose
    var origTimeMin: String? = null
    @SerializedName("@origTimeDate")
    @Expose
    var origTimeDate: String? = null
    @SerializedName("@destTimeMin")
    @Expose
    var destTimeMin: String? = null
    @SerializedName("@destTimeDate")
    @Expose
    var destTimeDate: String? = null
    @SerializedName("@line")
    @Expose
    var line: String? = null
    @SerializedName("@bikeflag")
    @Expose
    var bikeflag: String? = null
    @SerializedName("@trainHeadStation")
    @Expose
    var trainHeadStation: String? = null
    @SerializedName("@load")
    @Expose
    var load: String? = null

}
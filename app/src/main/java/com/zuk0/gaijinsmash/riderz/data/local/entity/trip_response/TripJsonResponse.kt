package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TripJsonResponse {
    @SerializedName("?xml")
    @Expose
    var xml: Xml? = null
    @SerializedName("root")
    @Expose
    var root: Root? = null

}
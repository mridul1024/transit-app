package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Fare {
    @SerializedName("@amount")
    @Expose
    var amount: String? = null
    @SerializedName("@class")
    @Expose
    var class_: String? = null
    @SerializedName("@name")
    @Expose
    var name: String? = null

}
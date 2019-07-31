package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Xml(

        @SerializedName("@version")
        @Expose
        var version : String? = null ,

        @SerializedName("@encoding")
        @Expose
        var encoding: String? = null
)
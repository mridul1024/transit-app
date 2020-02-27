package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Xml

class BsaJsonResponse {

    @SerializedName("?xml")
    @Expose
    var xml: Xml? = null

    @SerializedName("root")
    @Expose
    var root: BsaRoot? = null

}
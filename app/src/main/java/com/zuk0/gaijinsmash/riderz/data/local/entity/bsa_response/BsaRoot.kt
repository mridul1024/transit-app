package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BsaRoot {

    @Expose
    @SerializedName("@id")
    var id: Int? = 0

    @Expose
    @SerializedName("date")
    var date: String? = null

    @Expose
    @SerializedName("time")
    var time: String? = null

    @Expose
    @SerializedName("bsa")
    var bsaList: MutableList<Bsa>? = null

}
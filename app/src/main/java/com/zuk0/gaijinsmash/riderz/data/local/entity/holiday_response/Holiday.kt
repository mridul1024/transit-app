package com.zuk0.gaijinsmash.riderz.data.local.entity.holiday_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class Holiday {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("date")
    @Expose
    var date: Date? = null
    @SerializedName("schedule_type")
    @Expose
    var scheduleType: String? = null

}
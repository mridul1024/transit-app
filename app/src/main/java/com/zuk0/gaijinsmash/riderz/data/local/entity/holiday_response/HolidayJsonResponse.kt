package com.zuk0.gaijinsmash.riderz.data.local.entity.holiday_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HolidayJsonResponse {
    @SerializedName("holidays")
    @Expose
    var holidayList: List<Holiday>? = null

}
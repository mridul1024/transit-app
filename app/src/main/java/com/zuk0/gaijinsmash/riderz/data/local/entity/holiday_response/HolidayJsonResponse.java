package com.zuk0.gaijinsmash.riderz.data.local.entity.holiday_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HolidayJsonResponse {

    @SerializedName("holidays")
    @Expose
    private List<Holiday> holidayList;

    public List<Holiday> getHolidayList() {
        return holidayList;
    }

    public void setHolidayList(List<Holiday> holidayList) {
        this.holidayList = holidayList;
    }
}

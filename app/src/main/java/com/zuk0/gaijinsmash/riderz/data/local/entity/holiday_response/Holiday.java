package com.zuk0.gaijinsmash.riderz.data.local.entity.holiday_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Holiday {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("schedule_type")
    @Expose
    private String scheduleType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }
}

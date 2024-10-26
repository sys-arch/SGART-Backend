package com.team1.sgart.backend.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public class DefaultWorkingHoursDTO {

    @JsonProperty("day_of_week")
    private int dayOfWeek;
    @JsonProperty("start_time")
    private LocalTime dayStartTime;
    @JsonProperty("end_time")
    private LocalTime dayEndTime;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getDayStartTime() {
        return dayStartTime;
    }

    public void setDayStartTime(LocalTime dayStartTime) {
        this.dayStartTime = dayStartTime;
    }

    public LocalTime getDayEndTime() {
        return dayEndTime;
    }

    public void setDayEndTime(LocalTime dayEndTime) {
        this.dayEndTime = dayEndTime;
    }
}

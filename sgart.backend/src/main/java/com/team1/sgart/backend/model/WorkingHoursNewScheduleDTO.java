package com.team1.sgart.backend.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkingHoursNewScheduleDTO {

    @JsonProperty("selectedDate")
    private LocalDate exceptionDate;

    @JsonProperty("startingTime")
    private LocalTime startTime;

    @JsonProperty("endingTime")
    private LocalTime endTime;

    @JsonProperty("reason")
    private String reason;

    public LocalDate getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(LocalDate exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

}

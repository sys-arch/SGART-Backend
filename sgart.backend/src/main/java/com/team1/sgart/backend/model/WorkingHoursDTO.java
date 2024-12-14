package com.team1.sgart.backend.model;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkingHoursDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("startingTime")
    private LocalTime startTime;

    @JsonProperty("endingTime")
    private LocalTime endTime;


    public WorkingHoursDTO(int id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}

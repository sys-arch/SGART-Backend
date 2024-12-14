package com.team1.sgart.backend.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AbsencesDTO {

    @JsonProperty("absenceId")
    private UUID absenceId;

    @JsonProperty("userId")
    private UUID userId;

    @JsonProperty("absenceStartDate")
    private LocalDate absenceStartDate;

    @JsonProperty("absenceEndDate")
    private LocalDate absenceEndDate;

    @JsonProperty("absenceAllDay")
    private boolean absenceAllDay;

    @JsonProperty("absenceStartTime")
    private LocalTime absenceStartTime;

    @JsonProperty("absenceEndTime")
    private LocalTime absenceEndTime;

    @JsonProperty("absenceReason")
    private String absenceReason;

    public AbsencesDTO() {
    	/*Constructor por defecto*/
    }

    // Getters and Setters
    public UUID getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(UUID absenceId) {
        this.absenceId = absenceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDate getAbsenceStartDate() {
        return absenceStartDate;
    }

    public void setAbsenceStartDate(LocalDate absenceStartDate) {
        this.absenceStartDate = absenceStartDate;
    }

    public LocalDate getAbsenceEndDate() {
        return absenceEndDate;
    }

    public void setAbsenceEndDate(LocalDate absenceEndDate) {
        this.absenceEndDate = absenceEndDate;
    }

    public boolean isAbsenceAllDay() {
        return absenceAllDay;
    }

    public void setAbsenceAllDay(boolean absenceAllDay) {
        this.absenceAllDay = absenceAllDay;
    }

    public LocalTime getAbsenceStartTime() {
        return absenceStartTime;
    }

    public void setAbsenceStartTime(LocalTime absenceStartTime) {
        this.absenceStartTime = absenceStartTime;
    }

    public LocalTime getAbsenceEndTime() {
        return absenceEndTime;
    }

    public void setAbsenceEndTime(LocalTime absenceEndTime) {
        this.absenceEndTime = absenceEndTime;
    }

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
    }
}
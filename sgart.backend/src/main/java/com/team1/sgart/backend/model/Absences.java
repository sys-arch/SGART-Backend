package com.team1.sgart.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "SGART_AbsencesTable")
public class Absences {

    @Id
    @GeneratedValue
    @Column(name = "absence_id", nullable = false, updatable = false)
    private UUID absenceId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "absence_start_date", nullable = false)
    private LocalDate absenceStartDate;

    @Column(name = "absence_end_date", nullable = false)
    private LocalDate absenceEndDate;

    @Column(name = "absence_all_day", nullable = false)
    private boolean absenceAllDay;

    @Column(name = "absence_start_time")
    private LocalTime absenceStartTime;

    @Column(name = "absence_end_time")
    private LocalTime absenceEndTime;

    @Column(name = "absence_reason", length = 255)
    private String absenceReason;
    
    public Absences() { /*Constructor por defecto*/
    	
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

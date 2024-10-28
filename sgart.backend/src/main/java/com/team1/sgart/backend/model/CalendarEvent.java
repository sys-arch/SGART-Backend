package com.team1.sgart.backend.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SGART_CalendarEvents")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "event_id")
    private UUID eventId;

    @Column(name = "event_title", nullable = false)
    private String eventTitle;

    @Column(name = "event_start_date", nullable = false)
    private LocalDate eventStartDate;

    @Column(name = "event_all_day", nullable = false)
    private boolean eventAllDay;

    @Column(name = "event_time_start", nullable = false)
    private LocalTime eventTimeStart;

    @Column(name = "event_time_end", nullable = false)
    private LocalTime eventTimeEnd;

    @Column(name = "event_frequency", nullable = false)
    private String eventFrequency;

    @Column(name = "event_repetitions_count")
    private Integer eventRepetitionsCount;

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public LocalDate getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(LocalDate eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public boolean isEventAllDay() {
        return eventAllDay;
    }

    public void setEventAllDay(boolean eventAllDay) {
        this.eventAllDay = eventAllDay;
    }

    public LocalTime getEventTimeStart() {
        return eventTimeStart;
    }

    public void setEventTimeStart(LocalTime eventTimeStart) {
        this.eventTimeStart = eventTimeStart;
    }

    public LocalTime getEventTimeEnd() {
        return eventTimeEnd;
    }

    public void setEventTimeEnd(LocalTime eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd;
    }

    public String getEventFrequency() {
        return eventFrequency;
    }

    public void setEventFrequency(String eventFrequency) {
        this.eventFrequency = eventFrequency;
    }

    public Integer getEventRepetitionsCount() {
        return eventRepetitionsCount;
    }

    public void setEventRepetitionsCount(Integer eventRepetitionsCount) {
        this.eventRepetitionsCount = eventRepetitionsCount;
    }

    
}

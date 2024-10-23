package com.team1.sgart.backend.modules;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CalendarEventDTO {
    
    @JsonProperty("event_title")
    private String eventTitle;
    @JsonProperty("event_start_date")
    private LocalDate eventStartDate;
    @JsonProperty("event_all_day")
    private boolean eventAllDay;
    @JsonProperty("event_time_start")
    private LocalTime eventTimeStart;
    @JsonProperty("event_time_end")
    private LocalTime eventTimeEnd;
    @JsonProperty("event_frequency")
    private String eventFrequency;
    @JsonProperty("event_repetitions_count")
    private Integer eventRepetitionsCount;

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

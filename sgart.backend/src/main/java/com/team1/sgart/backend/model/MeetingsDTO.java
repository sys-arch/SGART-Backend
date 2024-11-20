package com.team1.sgart.backend.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetingsDTO {

    @JsonProperty("meetingId")
    private UUID meetingId;

    @JsonProperty("title")
    private String meetingTitle;

    @JsonProperty("allDay")
    private boolean meetingAllDay;

    @JsonProperty("meetingDate")
    private LocalDate meetingDate;

    @JsonProperty("startTime")
    private LocalTime meetingStartTime;

    @JsonProperty("endTime")
    private LocalTime meetingEndTime;

    @JsonProperty("organizerId")
    private UUID organizerId;

    @JsonProperty("organizerName")
    private String organizerName;

    @JsonProperty("observations")
    private String meetingObservations;

    @JsonProperty("locationName")
    private String locationName;

    public MeetingsDTO() {}

    public MeetingsDTO(UUID meetingId, String meetingTitle, boolean meetingAllDay, LocalDate meetingDate,
                       LocalTime meetingStartTime, LocalTime meetingEndTime, String meetingObservations,
                       UUID organizerId, String organizerName, String locationName) {
        this.meetingId = meetingId;
        this.meetingTitle = meetingTitle;
        this.meetingAllDay = meetingAllDay;
        this.meetingDate = meetingDate;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.meetingObservations = meetingObservations;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public UUID getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(UUID meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public boolean isMeetingAllDay() {
        return meetingAllDay;
    }

    public void setMeetingAllDay(boolean meetingAllDay) {
        this.meetingAllDay = meetingAllDay;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public LocalTime getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(LocalTime meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public LocalTime getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(LocalTime meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public UUID getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(UUID organizerId) {
        this.organizerId = organizerId;
    }

    public String getOrganizerName() {
        return organizerName;
    }
    
    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getMeetingObservations() {
        return meetingObservations;
    }

    public void setMeetingObservations(String meetingObservations) {
        this.meetingObservations = meetingObservations;
    }
    
}


package com.team1.sgart.backend.model;

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

    @JsonProperty("startTime")
    private LocalTime meetingStartTime;

    @JsonProperty("endTime")
    private LocalTime meetingEndTime;

    @JsonProperty("organizerId")
    private UUID organizerId;

    public MeetingsDTO() {}

    public MeetingsDTO(UUID meetingId, String meetingTitle, boolean meetingAllDay, LocalTime meetingStartTime, LocalTime meetingEndTime, UUID organizerId) {
        this.meetingId = meetingId;
        this.meetingTitle = meetingTitle;
        this.meetingAllDay = meetingAllDay;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.organizerId = organizerId;
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

}

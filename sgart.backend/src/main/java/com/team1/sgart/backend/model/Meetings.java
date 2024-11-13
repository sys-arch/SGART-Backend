package com.team1.sgart.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "SGART_MeetingsTable")
public class Meetings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meeting_id")
    private UUID meetingId;

    @Column(name = "meeting_title", nullable = false, length = 255)
    private String meetingTitle;

    @Column(name = "meeting_all_day", nullable = false)
    private boolean meetingAllDay;

    @Column(name = "meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Column(name = "meeting_start_time", nullable = false)
    private LocalTime meetingStartTime;

    @Column(name = "meeting_end_time", nullable = false)
    private LocalTime meetingEndTime;

    @Column(name = "organizer_id", nullable = false)
    private UUID organizerId;

    public Meetings() {}

    public Meetings(String meetingTitle, boolean meetingAllDay, LocalTime meetingStartTime, LocalTime meetingEndTime, UUID organizerId) {
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

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }
}

package com.team1.sgart.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "SGART_MeetingsTable")
public class Meetings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meeting_id", columnDefinition = "uniqueidentifier")
    private UUID meetingId;

    @Column(name = "meeting_title", nullable = false, length = 255)
    private String meetingTitle;

    @Column(name = "meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Column(name = "meeting_all_day", nullable = false)
    private boolean meetingAllDay;

    @Column(name = "meeting_start_time", nullable = false)
    private LocalTime meetingStartTime;

    @Column(name = "meeting_end_time", nullable = false)
    private LocalTime meetingEndTime;

    @Column(name = "meeting_observations", nullable = false, length = 255)
    private String meetingObservations;

    @Column(name = "organizer_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID organizerId;

    @Column(name = "location_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID locationId;

    // Default constructor
    public Meetings() {}

    // Constructor with parameters
    public Meetings(String meetingTitle, LocalDate meetingDate, boolean meetingAllDay, LocalTime meetingStartTime,
                    LocalTime meetingEndTime, String meetingObservations, UUID organizerId, UUID locationId) {
        this.meetingTitle = meetingTitle;
        this.meetingDate = meetingDate;
        this.meetingAllDay = meetingAllDay;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.meetingObservations = meetingObservations;
        this.organizerId = organizerId;
        this.locationId = locationId;
    }

    // Getters and setters
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

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
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

    public String getMeetingObservations() {
        return meetingObservations;
    }

    public void setMeetingObservations(String meetingObservations) {
        this.meetingObservations = meetingObservations;
    }

    public UUID getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(UUID organizerId) {
        this.organizerId = organizerId;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }
}
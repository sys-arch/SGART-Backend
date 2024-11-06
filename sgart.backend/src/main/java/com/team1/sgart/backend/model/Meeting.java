package com.team1.sgart.backend.model;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "SGART_MeetingsTable")
public class Meeting {

    @Id
    @GeneratedValue
    @Column(name = "meeting_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "meeting_title", nullable = false, length = 255)
    private String title;

    @Column(name = "meeting_all_day", nullable = false)
    private boolean allDay;

    @Column(name = "meeting_start_time", nullable = false)
    private Time startTime;

    @Column(name = "meeting_end_time", nullable = false)
    private Time endTime;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToMany
    @JoinTable(
        name = "SGART_MeetingAttendeesTable",
        joinColumns = @JoinColumn(name = "meeting_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> attendees;//--------------------------------------revisar esto--------------------------------

    @Column(name = "meeting_location", nullable = false, length = 100)
    private String location;

    @Column(name = "meeting_observations", columnDefinition = "TEXT")
    private String observations;


    public Meeting() {} 

    public Meeting(String title, boolean allDay, Time startTime, Time endTime, User organizer, String location, String observations) {
        this.title = title;
        this.allDay = allDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.organizer = organizer;
        this.location = location;
        this.observations = observations;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public List<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<User> attendees) {
		this.attendees = attendees;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

    // Getters y setters 
    
}

package com.team1.sgart.backend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SGART_InvitationsTable")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "invitation_status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @Column(name = "user_attendance", nullable = false)
    private boolean userAttendance;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    public Invitation() {}

    public Invitation(Meeting meeting, User user, InvitationStatus status, boolean userAttendance, String rejectionReason) {
        this.meeting = meeting;
        this.user = user;
        this.status = status;
        this.userAttendance = userAttendance;
        this.rejectionReason = rejectionReason;
    }


    // Getters y setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public InvitationStatus getStatus() {
		return status;
	}

	public void setStatus(InvitationStatus status) {
		this.status = status;
	}

	public boolean isUserAttendance() {
		return userAttendance;
	}

	public void setUserAttendance(boolean userAttendance) {
		this.userAttendance = userAttendance;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

    
}

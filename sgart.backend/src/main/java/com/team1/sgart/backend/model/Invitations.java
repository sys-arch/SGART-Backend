package com.team1.sgart.backend.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "SGART_InvitationsTable")
public class Invitations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invitationId;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meetings meeting;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "invitation_status", nullable = false, length = 50)
    private InvitationStatus invitationStatus;

    @Column(name = "user_attendance", nullable = false)
    private boolean userAttendance;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    public Invitations() {}

    public Invitations(Meetings meeting, UUID user, InvitationStatus invitationStatus, boolean userAttendance, String rejectionReason) {
        this.meeting = meeting;
        this.userId = user;
        this.invitationStatus = invitationStatus;
        this.userAttendance = userAttendance;
        this.rejectionReason = rejectionReason;
    }

    public int getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
    }

    public Meetings getMeeting() {
        return meeting;
    }

    public void setMeeting(Meetings meeting) {
        this.meeting = meeting;
    }

    public UUID getUser() {
        return userId;
    }

    public void setUser(UUID user) {
        this.userId = user;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
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

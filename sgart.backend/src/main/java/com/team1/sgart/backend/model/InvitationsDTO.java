package com.team1.sgart.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class InvitationsDTO {

    @JsonProperty("invitationId")
    private UUID invitationId;  // Cambiado a UUID

    @JsonProperty("meetingId")
    private UUID meetingId;

    @JsonProperty("userId")
    private UUID userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("status")
    private String invitationStatus;

    @JsonProperty("attendance")
    private boolean userAttendance;

    @JsonProperty("rejectionReason")
    private String rejectionReason;

    public InvitationsDTO() {
    }

    public InvitationsDTO(UUID invitationId, UUID meetingId, UUID userId, String
                                  userName, String invitationStatus,
                          boolean userAttendance, String rejectionReason) {
        this.invitationId = invitationId;
        this.meetingId = meetingId;
        this.userId = userId;
        this.userName = userName;
        this.invitationStatus = invitationStatus;
        this.userAttendance = userAttendance;
        this.rejectionReason = rejectionReason;
    }

    public UUID getInvitationId() {  // Cambiado a UUID
        return invitationId;
    }

    public void setInvitationId(UUID invitationId) {  // Cambiado a UUID
        this.invitationId = invitationId;
    }

    public UUID getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(UUID meetingId) {
        this.meetingId = meetingId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(String invitationStatus) {
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

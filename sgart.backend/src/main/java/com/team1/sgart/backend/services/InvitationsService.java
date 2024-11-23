package com.team1.sgart.backend.services;

import java.util.UUID;
import com.team1.sgart.backend.dao.InvitationsDao;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InvitationsService {

    private static final Logger logger = LoggerFactory.getLogger(InvitationsService.class);
    private final InvitationsDao invitationsDao;


    @Autowired
    public InvitationsService(InvitationsDao invitationsDao) {
        this.invitationsDao = invitationsDao;
        logger.info("[!] InvitationsService created");
    }

    public boolean updateInvitationStatus(UUID meetingId, UUID userId, String newStatus, String comment) {
        try {
            logger.debug("Updating invitation - Meeting: {}, User: {}, Status: {}", 
                      meetingId, userId, newStatus);
            
            int count = invitationsDao.countByMeetingIdAndUserId(meetingId, userId);
            if (count == 0) {
                logger.warn("Invitation not found");
                return false;
            }
            
            return invitationsDao.updateInvitationStatus(meetingId, userId, newStatus, comment) > 0;
        } catch (Exception e) {
            logger.error("Error updating invitation status", e);
            throw new RuntimeException("Error updating invitation status", e);
        }
    }

    public boolean updateUserAttendance(UUID meetingId, UUID userId) {
        try {
            logger.debug("Updating user attendance - Meeting: {}, User: {}", 
                      meetingId, userId);
            
            int rowsUpdated = invitationsDao.updateUserAttendance(meetingId, userId);
            return rowsUpdated > 0;
            
        } catch (Exception e) {
            logger.error("Error updating user attendance", e);
            throw new RuntimeException("Error updating user attendance", e);
        }
    }

    public Integer getUserAttendance(UUID meetingId, UUID userId) {
        if (meetingId == null || userId == null) {
            logger.error("Meeting ID or User ID is null");
            throw new IllegalArgumentException("Meeting ID and User ID cannot be null");
        }

        try {
            logger.debug("Getting user attendance - Meeting: {}, User: {}", 
                      meetingId, userId);
            Integer attendance = invitationsDao.getUserAttendance(meetingId, userId);
            
            if (attendance == null) {
                logger.debug("No attendance record found for Meeting: {} and User: {}", 
                          meetingId, userId);
            }
            
            return attendance;
            
        } catch (Exception e) {
            logger.error("Error getting user attendance for Meeting: {} and User: {}: {}", 
                      meetingId, userId, e.getMessage(), e);
            throw new RuntimeException("Error retrieving user attendance: " + e.getMessage(), e);
        }
    }

}

package com.team1.sgart.backend.services;

import java.util.List;
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

    public boolean inviteUsers(UUID meetingId, List<UUID> userIds) {
        if (meetingId == null || userIds == null || userIds.isEmpty()) {
            logger.error("Meeting ID is null or users list is empty");
            return false;
        }

        try {
            logger.debug("Creating invitations for meeting: {} and {} users", 
                      meetingId, userIds.size());
            
            // Por cada usuario en la lista, crear una invitación
            int successCount = 0;
            for (UUID userId : userIds) {
                // Verificar si ya existe una invitación para este usuario en esta reunión
                if (invitationsDao.countByMeetingIdAndUserId(meetingId, userId) > 0) {
                    logger.warn("Invitation already exists for user {} in meeting {}", 
                             userId, meetingId);
                    continue;
                }
                
                // Crear nueva invitación con estado "Pendiente"
                int result = invitationsDao.createInvitation(
                    meetingId,
                    userId,
                    "Pendiente",
                    null
                );
                if (result > 0) {
                    successCount++;
                }
            }
            
            logger.info("Successfully created {} invitations out of {} users", 
                     successCount, userIds.size());
            
            return successCount > 0;
            
        } catch (Exception e) {
            logger.error("Error creating invitations for meeting {}: {}", 
                      meetingId, e.getMessage(), e);
            throw new RuntimeException("Error creating invitations", e);
        }
    }

}

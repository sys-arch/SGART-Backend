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

}

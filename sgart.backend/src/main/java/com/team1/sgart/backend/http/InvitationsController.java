package com.team1.sgart.backend.http;

import com.team1.sgart.backend.services.InvitationsService;
import com.team1.sgart.backend.util.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/invitations")
public class InvitationsController {

    private static final Logger logger = LoggerFactory.getLogger(InvitationsController.class);
    private final InvitationsService invitationsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public InvitationsController(InvitationsService invitationsService, JwtTokenProvider jwtTokenProvider) {
        this.invitationsService = invitationsService;
        this.jwtTokenProvider = jwtTokenProvider;
        logger.info("[!] InvitationsController created");
    }

    private UUID getCurrentUserId() {
        try {
            String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            logger.debug("Extracted token: {}", token);

            String userId = jwtTokenProvider.getUserIdFromToken(token);
            logger.debug("Extracted userId: {}", userId);

            if (userId == null || userId.isEmpty()) {
                logger.warn("userId is null or empty");
                return null;
            }
            return UUID.fromString(userId);
        } catch (Exception e) {
            logger.error("Error extracting userId from token: {}", e.getMessage(), e);
            return null;
        }
    }



    @PutMapping("/{meetingId}/status")
    public ResponseEntity<?> updateInvitationStatus(
            @PathVariable UUID meetingId,
            @RequestBody Map<String, String> requestBody) {

        UUID userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            boolean updated = invitationsService.updateInvitationStatus(
                    meetingId,
                    userId,
                    requestBody.get("newStatus"),
                    requestBody.get("comment"));

            return updated ? ResponseEntity.ok().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating invitation status: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error updating invitation status");
        }
    }

    @PutMapping("/{meetingId}/attendance")
    public ResponseEntity<?> updateUserAttendance(@PathVariable UUID meetingId) {
        UUID userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            boolean updated = invitationsService.updateUserAttendance(meetingId, userId);
            return updated ? ResponseEntity.ok().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating user attendance: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error updating user attendance");
        }
    }

    @GetMapping("/{meetingId}/attendance")
    public ResponseEntity<?> getUserAttendance(@PathVariable UUID meetingId) {
        UUID userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Integer attendance = invitationsService.getUserAttendance(meetingId, userId);
            return attendance != null
                    ? ResponseEntity.ok(attendance)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error getting user attendance: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error getting user attendance");
        }
    }

    @PostMapping("/{meetingId}/invite")
    public ResponseEntity<?> inviteUsers(
            @PathVariable UUID meetingId,
            @RequestBody List<UUID> userIds) {

        logger.debug("Entering inviteUsers with meetingId: {} and userIds: {}", meetingId, userIds);

        UUID userId = getCurrentUserId();
        if (userId == null) {
            logger.warn("Unauthorized access: userId is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        logger.debug("Current userId extracted from token: {}", userId);

        try {
            logger.debug("Invoking invitationsService.inviteUsers with meetingId: {} and userIds: {}", meetingId, userIds);
            boolean invited = invitationsService.inviteUsers(meetingId, userIds);

            if (invited) {
                logger.info("Successfully invited users to meeting with meetingId: {}", meetingId);
                return ResponseEntity.ok().build();
            } else {
                logger.warn("Failed to invite users to meeting with meetingId: {}", meetingId);
                return ResponseEntity.badRequest().body("Error inviting users");
            }
        } catch (Exception e) {
            logger.error("Error inviting users to meeting with meetingId: {}. Exception: {}", meetingId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error processing invitations");
        }
    }

}

package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team1.sgart.backend.services.InvitationsService;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/invitations")
@CrossOrigin(origins = "http://localhost:3000")

public class InvitationsController {

    private static final Logger logger = LoggerFactory.getLogger(InvitationsController.class);
    private final InvitationsService invitationsService;

    @Autowired
    public InvitationsController(InvitationsService invitationsService) {
        this.invitationsService = invitationsService;
        logger.info("[!] InvitationsController created");
    }

    @PatchMapping("/{meetingId}/status")
    public ResponseEntity<?> updateInvitationStatus(
            @PathVariable UUID meetingId,
            @RequestBody Map<String, String> requestBody,
            HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
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

}

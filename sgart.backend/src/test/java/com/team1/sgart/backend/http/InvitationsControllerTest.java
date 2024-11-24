package com.team1.sgart.backend.http;

import com.team1.sgart.backend.services.InvitationsService;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class InvitationsControllerTest {

    @Mock
    private InvitationsService invitationsService;

    @InjectMocks
    private InvitationsController invitationsController;

    private UUID meetingId;
    private UUID userId;
    private Map<String, String> requestBody;
    private HttpSession session;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invitationsService = mock(InvitationsService.class);
        invitationsController = new InvitationsController(invitationsService);
        meetingId = UUID.randomUUID();
        userId = UUID.randomUUID();
        requestBody = new HashMap<>();
        session = mock(HttpSession.class);
        when(session.getAttribute("userId")).thenReturn(userId);
    }

    @Test
    void updateInvitationStatus_WhenSuccessful_ReturnsOk() {
        // Arrange
        requestBody.put("newStatus", "Aceptada");
        when(session.getAttribute("userId")).thenReturn(userId);
        when(invitationsService.updateInvitationStatus(any(), any(), any(), any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
            meetingId, requestBody, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateInvitationStatus_WhenNotFound_ReturnsNotFound() {
        // Arrange
        requestBody.put("newStatus", "Aceptada");
        when(session.getAttribute("userId")).thenReturn(userId);
        when(invitationsService.updateInvitationStatus(any(), any(), any(), any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
            meetingId, requestBody, session);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateInvitationStatus_WhenRejected_ReturnsOk() {
        // Arrange
        requestBody.put("newStatus", "Rechazada");
        requestBody.put("comment", "No puedo asistir");
        when(session.getAttribute("userId")).thenReturn(userId);
        when(invitationsService.updateInvitationStatus(any(), any(), any(), any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
            meetingId, requestBody, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateInvitationStatus_WhenServiceThrowsException_ReturnsBadRequest() {
        // Arrange
        requestBody.put("newStatus", "Aceptada");
        when(session.getAttribute("userId")).thenReturn(userId);
        when(invitationsService.updateInvitationStatus(any(), any(), any(), any()))
            .thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
            meetingId, requestBody, session);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error updating invitation status", response.getBody());
    }

    @Test
    void updateInvitationStatus_WhenInvalidStatus_ReturnsBadRequest() {
        // Arrange
        requestBody.put("newStatus", "InvalidStatus");
        when(session.getAttribute("userId")).thenReturn(userId);
        when(invitationsService.updateInvitationStatus(any(), any(), any(), any()))
            .thenThrow(new IllegalArgumentException("Invalid status"));

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
            meetingId, requestBody, session);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

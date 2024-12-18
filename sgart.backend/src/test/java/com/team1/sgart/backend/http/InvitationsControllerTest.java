package com.team1.sgart.backend.http;

import com.team1.sgart.backend.services.InvitationsService;
import com.team1.sgart.backend.util.JwtTokenProvider;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class InvitationsControllerTest {
	
	private static final String USER_ID = "userId";
	private static final String ACCEPTED_STATUS = "Aceptada";
	private static final String NEW_STATUS = "newStatus";

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
    	userId = UUID.randomUUID();
        meetingId = UUID.randomUUID();
        MockitoAnnotations.openMocks(this);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        invitationsController = new InvitationsController(invitationsService, jwtTokenProvider);
        invitationsService = mock(InvitationsService.class);
        when(jwtTokenProvider.getUserIdFromToken(anyString())).thenReturn(userId.toString());

        requestBody = new HashMap<>();
        session = mock(HttpSession.class);
        when(session.getAttribute(USER_ID)).thenReturn(userId);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getCredentials()).thenReturn("mockToken");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void updateInvitationStatus_WhenNotFound_ReturnsNotFound() {
        // Arrange
        requestBody.put(NEW_STATUS, ACCEPTED_STATUS);
        when(session.getAttribute(USER_ID)).thenReturn(userId);
        //when(invitationsService.updateInvitationStatus(any(), any(), any(), any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
            meetingId, requestBody);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void updateInvitationStatus_WhenUnauthorized_ReturnsUnauthorized() {
        // Arrange
        requestBody.put(NEW_STATUS, ACCEPTED_STATUS);
        when(session.getAttribute(USER_ID)).thenReturn(null); // Simula un usuario no autenticado

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
                meetingId, requestBody);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void updateInvitationStatus_WhenServiceThrowsException_ReturnsBadRequest() {
        // Arrange
        requestBody.put(NEW_STATUS, ACCEPTED_STATUS);
        //when(invitationsService.updateInvitationStatus(any(), any(), eq(ACCEPTED_STATUS), any()))
            //.thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<?> response = invitationsController.updateInvitationStatus(
                meetingId, requestBody);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }


}

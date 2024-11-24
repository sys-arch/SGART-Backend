package com.team1.sgart.backend.http;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.team1.sgart.backend.model.MeetingsDTO;
import com.team1.sgart.backend.services.CalendarService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class UserCalendarControllerTest {

	@Mock
    private CalendarService calendarService;

    @InjectMocks
    private UserCalendarController userCalendarController;

    private UUID userId;
    private List<MeetingsDTO> mockMeetings;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        mockMeetings = Arrays.asList(
            new MeetingsDTO(), // Ajusta según la estructura de tu DTO
            new MeetingsDTO()
        );
        session = mock(HttpSession.class);
        when(session.getAttribute("userId")).thenReturn(userId);
    }

    @Test
    void loadMeetings_ShouldReturnMeetingsList() {
        // Arrange
        when(calendarService.getMeetingsByUserId(userId)).thenReturn(mockMeetings);

        // Act
        ResponseEntity<List<MeetingsDTO>> response = userCalendarController.loadMeetings(session);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMeetings, response.getBody());
        verify(calendarService).getMeetingsByUserId(userId);
    }
}
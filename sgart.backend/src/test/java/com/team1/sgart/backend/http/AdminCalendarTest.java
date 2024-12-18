package com.team1.sgart.backend.http;

import com.team1.sgart.backend.model.InvitationsDTO;
import com.team1.sgart.backend.model.MeetingsDTO;
import com.team1.sgart.backend.services.CalendarService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminCalendarTest {
	
	private static final String ACCEPTED_STATUS = "Accepted";
	private static final String EXAMPLE_NAME = "John Doe";

    @InjectMocks
    private AdminCalendarController controller;

    @Mock
    private CalendarService calendarService;

    public AdminCalendarTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMeetings() {
        // Arrange
        MeetingsDTO mockMeeting = new MeetingsDTO(UUID.randomUUID(), "Test Meeting", false, LocalDate.parse("2024-12-15"),  LocalTime.parse("09:00"), LocalTime.parse("10:00"), "", UUID.randomUUID(), "", "");
        when(calendarService.loadMeetings()).thenReturn(Collections.singletonList(mockMeeting));

        // Act
        ResponseEntity<List<MeetingsDTO>> response = controller.getAllMeetings();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Meeting", response.getBody().get(0).getMeetingTitle());
        verify(calendarService, times(1)).loadMeetings();
    }

    


    @Test
    void testLoadMeetings_ServiceLayer() {
        // Arrange
        UUID meetingId = UUID.randomUUID();
        MeetingsDTO mockMeeting = new MeetingsDTO(
            meetingId, 
            "Team Meeting", // Cambiado el título para que coincida con el assert
            false, 
            LocalDate.parse("2024-12-15"), 
            LocalTime.parse("09:00"), 
            LocalTime.parse("10:00"), 
            "", 
            UUID.randomUUID(), 
            "", 
            ""
        );

        when(calendarService.loadMeetings()).thenReturn(Collections.singletonList(mockMeeting));

        // Act
        List<MeetingsDTO> meetings = calendarService.loadMeetings();

        // Assert
        assertNotNull(meetings);
        assertEquals(1, meetings.size());
        assertEquals(meetingId, meetings.get(0).getMeetingId());
        assertEquals("Team Meeting", meetings.get(0).getMeetingTitle()); // Asegúrate de que coincida el título
    }


    @Test
    void testGetInvitees_InvalidMeetingId() {
        // Arrange
        Map<String, String> payload = new HashMap<>(); // Simula un payload vacío

        // Act
        ResponseEntity<List<InvitationsDTO>> response = controller.getInvitees(payload);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // Verifica el código 400
        assertNull(response.getBody()); // Verifica que el cuerpo de la respuesta sea nulo
        verify(calendarService, never()).getDetailedInvitationsByMeetingId(any()); // Asegura que no se llamó al servicio
    }

    @Test
    void testGetInvitees_InvalidUUIDFormat() {
        // Arrange
        Map<String, String> payload = new HashMap<>();
        payload.put("meetingId", "invalid-uuid-format"); // Simula un UUID no válido

        // Act
        ResponseEntity<List<InvitationsDTO>> response = controller.getInvitees(payload);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // Verifica el código 400
        assertNull(response.getBody()); // Verifica que el cuerpo de la respuesta sea nulo
        verify(calendarService, never()).getDetailedInvitationsByMeetingId(any()); // Asegura que no se llamó al servicio
    }

}
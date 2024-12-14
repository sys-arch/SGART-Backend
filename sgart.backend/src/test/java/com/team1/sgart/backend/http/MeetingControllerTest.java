package com.team1.sgart.backend.http;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team1.sgart.backend.dao.InvitationsDao;
import com.team1.sgart.backend.dao.MeetingsDao;
import com.team1.sgart.backend.model.Meetings;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.services.MeetingService;
import com.team1.sgart.backend.services.UserService;
import com.team1.sgart.backend.util.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(MeetingController.class)
class MeetingControllerTest {
	
	private static final String URL_MEETINGS = "/api/meetings/";
	private static final String URL_MODIFY = "/modify";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MeetingService meetingService;
    
    @MockBean
    private InvitationsDao invitationDao;

    @MockBean
    private UserService userService;
    
    @Mock
    private MeetingsDao meetingDao;

    @Autowired
    private ObjectMapper objectMapper;
    
    private Meetings existingMeeting;
    private Meetings updatedMeeting;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(jwtTokenProvider.validateToken("fake-jwt-token")).thenReturn(true);
        Mockito.when(jwtTokenProvider.getEmailFromToken("fake-jwt-token")).thenReturn("user@example.com");

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Datos iniciales
        existingMeeting = new Meetings(
                "Initial Meeting", 
                LocalDate.parse("2024-12-12"), 
                false, 
                LocalTime.of(9, 0), 
                LocalTime.of(10, 0), 
                "Initial Observations", 
                UUID.randomUUID(), 
                UUID.randomUUID()
        );
        existingMeeting.setMeetingId((UUID.fromString("fc5e6d02-8c77-4e24-9372-8e8a9e876d91")));
    }

    @Test
    void createMeeting_ShouldReturnMeeting() throws Exception {
        Meetings meeting = new Meetings();
        meeting.setMeetingTitle("Reunión de prueba");
        meeting.setMeetingAllDay(false);
        meeting.setMeetingStartTime(LocalTime.of(10, 0, 0));
        meeting.setMeetingEndTime(LocalTime.of(12, 0, 0));
        meeting.setOrganizerId(UUID.randomUUID());
        meeting.setLocationId(UUID.randomUUID());
        meeting.setMeetingObservations("Reunión importante");

        Mockito.when(meetingService.createMeeting(
                Mockito.anyString(), 
                Mockito.anyBoolean(), 
                Mockito.any(), 
                Mockito.any(), 
                Mockito.any(), 
                Mockito.anyString(), 
                Mockito.any(), 
                Mockito.any()
        )).thenReturn(meeting);

        mockMvc.perform(post("/api/meetings/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meeting))
                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingTitle").value("Reunión de prueba"))
                .andExpect(jsonPath("$.meetingAllDay").value(false));
    }

    
    @Test
    void getAttendees_ShouldReturnListOfAttendees() throws Exception {
        UUID meetingId = UUID.randomUUID();
        Meetings meeting = new Meetings();
        meeting.setMeetingId(meetingId);

        User user1 = new User();
        user1.setID(UUID.randomUUID());

        User user2 = new User();
        user2.setID(UUID.randomUUID());

        List<UUID> attendees = List.of(user1.getID(), user2.getID());

        Mockito.when(meetingService.getMeetingById(meetingId))
                .thenReturn(Optional.of(meeting));
        Mockito.when(meetingService.getAttendeesForMeeting(meeting))
                .thenReturn(attendees);

        mockMvc.perform(get("/api/meetings/{meetingId}/attendees", meetingId)
                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(user1.getID().toString()))
                .andExpect(jsonPath("$[1]").value(user2.getID().toString()));
    }

    

    @Test
    void cancelMeeting_ShouldReturnSuccessMessage() throws Exception {
        UUID meetingId = UUID.randomUUID();

        Mockito.when(meetingService.cancelMeetingByOrganizer(Mockito.eq(meetingId))).thenReturn(true);

        mockMvc.perform(delete("/api/meetings/{meetingId}/cancel", meetingId)
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reunión cancelada exitosamente"));

        Mockito.verify(meetingService, Mockito.times(1)).cancelMeetingByOrganizer(Mockito.eq(meetingId));
    }
    @Test
    void getAvailableUsers_ShouldReturnListOfUsers() throws Exception {
        User user1 = new User();
        user1.setID(UUID.randomUUID());
        user1.setName("John Doe");

        User user2 = new User();
        user2.setID(UUID.randomUUID());
        user2.setName("Jane Doe");

        List<User> availableUsers = List.of(user1, user2);

        Mockito.when(meetingService.getAvailableUsers()).thenReturn(availableUsers);

        mockMvc.perform(get("/api/meetings/available-users")
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getID().toString()))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(user2.getID().toString()))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        Mockito.verify(meetingService, Mockito.times(1)).getAvailableUsers();
    }
    @Test
    void editMeeting_InvalidMeetingId_ShouldReturnNotFound() throws Exception {
        UUID invalidMeetingId = UUID.randomUUID();
        Meetings updatedMeeting = new Meetings(
                "Updated Meeting", 
                LocalDate.parse("2024-12-15"), 
                false, 
                LocalTime.of(10, 0), 
                LocalTime.of(11, 0), 
                "Updated Observations", 
                UUID.randomUUID(), 
                UUID.randomUUID()
        );

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunión no encontrada"))
                .when(meetingService).modifyMeeting(Mockito.eq(invalidMeetingId), Mockito.any(Meetings.class));

        // Ejecutar la petición y verificar resultados
        mockMvc.perform(post("/api/meetings/{meetingId}/modify", invalidMeetingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMeeting))
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reunión no encontrada"));

        Mockito.verify(meetingService, Mockito.times(1))
                .modifyMeeting(Mockito.eq(invalidMeetingId), Mockito.any(Meetings.class));
    }
    @Test
    void cancelMeeting_NonExistentMeeting_ShouldReturnNotFound() throws Exception {
        UUID nonExistentMeetingId = UUID.randomUUID();

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunión no encontrada"))
                .when(meetingService).cancelMeetingByOrganizer(Mockito.eq(nonExistentMeetingId));

        mockMvc.perform(delete("/api/meetings/{meetingId}/cancel", nonExistentMeetingId)
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isNotFound()); // Verificar solo el estado

        Mockito.verify(meetingService, Mockito.times(1)).cancelMeetingByOrganizer(Mockito.eq(nonExistentMeetingId));
    }


}

package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.InvitationsDTO;
import com.team1.sgart.backend.model.MeetingsDTO;
import com.team1.sgart.backend.services.CalendarService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/usuarios/calendarios")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserCalendarController {

    private static final Logger logger = LoggerFactory.getLogger(UserCalendarController.class);
    private final CalendarService calendarService;

    @Autowired
    public UserCalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
        logger.info("[!] UserCalendarController created");
    }
    
    @GetMapping("/meetings")
    public ResponseEntity<List<MeetingsDTO>> loadMeetings(HttpSession session) {
            UUID userId = (UUID) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
    
            // Obtener reuniones asociadas al usuario
            List<MeetingsDTO> meetings = calendarService.getMeetingsByUserId(userId);
            return ResponseEntity.ok(meetings);
        }
    
        @PostMapping("/invitados")
        public ResponseEntity<List<InvitationsDTO>> loadInvitees(@RequestBody UUID meetingId, HttpSession session) {
            UUID userId = (UUID) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
    
            // Validar que el usuario esté autorizado para ver los detalles de la reunión
            if (!calendarService.isUserAuthorizedForMeeting(userId, meetingId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
    
            // Obtener los detalles de las invitaciones para la reunión
            List<InvitationsDTO> invitees = calendarService.getDetailedInvitationsByMeetingId(meetingId);
        return ResponseEntity.ok(invitees);
    }
    
}

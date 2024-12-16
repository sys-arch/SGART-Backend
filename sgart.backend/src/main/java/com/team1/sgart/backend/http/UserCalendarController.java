package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.team1.sgart.backend.model.InvitationsDTO;
import com.team1.sgart.backend.model.MeetingsDTO;
import com.team1.sgart.backend.services.CalendarService;
import org.springframework.web.server.ResponseStatusException;
import com.team1.sgart.backend.util.JwtTokenProvider;



import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/usuarios/calendarios")
public class UserCalendarController {

    private static final Logger logger = LoggerFactory.getLogger(UserCalendarController.class);
    private final CalendarService calendarService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserCalendarController(CalendarService calendarService, JwtTokenProvider jwtTokenProvider) {
        this.calendarService = calendarService;
        this.jwtTokenProvider = jwtTokenProvider;
        logger.info("[!] UserCalendarController created");
    }

    private UUID extractUserIdFromToken(String token) {
        try {
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            return UUID.fromString(userId);
        } catch (Exception e) {
            logger.error("Error extracting userId from token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }
    }

    @GetMapping("/meetings")
    public ResponseEntity<List<MeetingsDTO>> loadMeetings(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        UUID userId = extractUserIdFromToken(token);
        logger.info("User ID: {}", userId);

        // Obtener reuniones asociadas al usuario
        List<MeetingsDTO> meetings = calendarService.getMeetingsByUserId(userId);
        return ResponseEntity.ok(meetings);
    }

    @PostMapping("/invitados")
    public ResponseEntity<List<InvitationsDTO>> loadInvitees(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UUID meetingId) {
        String token = authorizationHeader.replace("Bearer ", "");
        UUID userId = extractUserIdFromToken(token);

        // Validar que el usuario esté autorizado para ver los detalles de la reunión
        if (!calendarService.isUserAuthorizedForMeeting(userId, meetingId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Obtener los detalles de las invitaciones para la reunión
        List<InvitationsDTO> invitees = calendarService.getDetailedInvitationsByMeetingId(meetingId);
        return ResponseEntity.ok(invitees);
    }

    @GetMapping("/organized-meetings")
    public ResponseEntity<List<MeetingsDTO>> loadOrganizedMeetings(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        UUID userId = extractUserIdFromToken(token);
        logger.info("User ID: {}", userId);

        // Obtener reuniones organizadas por el usuario
        List<MeetingsDTO> meetings = calendarService.getOrganizedMeetingsByUserId(userId);
        return ResponseEntity.ok(meetings);
    }
}


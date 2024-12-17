package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.InvitationsDTO;
import com.team1.sgart.backend.model.MeetingsDTO;
import com.team1.sgart.backend.services.CalendarService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/administrador/calendarios")
public class AdminCalendarController {

    private static final Logger logger = LoggerFactory.getLogger(AdminCalendarController.class);
    private final CalendarService calendarService;

    @Autowired
    public AdminCalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
        logger.info("[!] AdminCalendarController created");
    }

    @GetMapping("/loadMeetings")
    public ResponseEntity<List<MeetingsDTO>> getAllMeetings() {
        List<MeetingsDTO> meetingsList = calendarService.loadMeetings();
        return ResponseEntity.ok(meetingsList);
    }

    @PostMapping("/invitados")
    public ResponseEntity<List<InvitationsDTO>> getInvitees(@RequestBody Map<String, String> payload) {
        String meetingIdString = payload.get("meetingId");
        if (meetingIdString == null || meetingIdString.isEmpty()) {
            logger.error("Error: meetingId es nulo o vacío");
            return ResponseEntity.badRequest().build();
        }

        UUID meetingId;
        try {
            meetingId = UUID.fromString(meetingIdString);
        } catch (IllegalArgumentException e) {
            logger.error("Error: meetingId no es un UUID válido", e);
            return ResponseEntity.badRequest().build();
        }

        List<InvitationsDTO> invitees = calendarService.getDetailedInvitationsByMeetingId(meetingId);
        if (invitees == null || invitees.isEmpty()) {
            return ResponseEntity.ok(List.of()); // Devuelve una lista vacía
        }
        return ResponseEntity.ok(invitees);
    }


}
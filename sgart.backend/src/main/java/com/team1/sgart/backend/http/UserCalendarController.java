package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/usuarios/calendarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UserCalendarController {

    private static final Logger logger = LoggerFactory.getLogger(UserCalendarController.class);
    private final CalendarService calendarService;

    @Autowired
    public UserCalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
        logger.info("[!] UserCalendarController created");
    }

    @GetMapping("/{userId}/meetings")
    public ResponseEntity<List<MeetingsDTO>> loadMeetings(@PathVariable UUID userId) {
        logger.info("Loading meetings for user: {}", userId);
        List<MeetingsDTO> meetings = calendarService.getMeetingsByUserId(userId);
        return ResponseEntity.ok(meetings);
    }

}

package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.MeetingsDTO;
import com.team1.sgart.backend.services.CalendarService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
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

    @GetMapping("/meetings")
    public ResponseEntity<List<MeetingsDTO>> loadMeetings(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<MeetingsDTO> meetings = calendarService.getMeetingsByUserId(userId);
        return ResponseEntity.ok(meetings);
    }

}

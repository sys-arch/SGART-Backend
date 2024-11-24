package com.team1.sgart.backend.http;

import com.team1.sgart.backend.model.Meetings;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.Locations;
import com.team1.sgart.backend.services.MeetingService;
import com.team1.sgart.backend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/meetings")
public class MeetingController {

    private MeetingService meetingService;
    private static final Logger logger = LoggerFactory.getLogger(MeetingController.class);

    @Autowired
    public MeetingController(MeetingService meetingService, UserService userService) {
        this.meetingService = meetingService;
    }

    // Crear una reunión
    @PostMapping("/create")
    public ResponseEntity<Meetings> createMeeting(@RequestBody Meetings meeting) {
        Meetings createdMeeting = meetingService.createMeeting(meeting.getMeetingTitle(), meeting.isMeetingAllDay(),
                meeting.getMeetingDate(),
                meeting.getMeetingStartTime(), meeting.getMeetingEndTime(), meeting.getMeetingObservations(),
                meeting.getOrganizerId(), meeting.getLocationId());

        return ResponseEntity.ok(createdMeeting);
    }

    // Obtener usuarios habilitados para ser invitados
    @GetMapping("/available-users")
    public ResponseEntity<List<User>> getAvailableUsers() {
        List<User> availableUsers = meetingService.getAvailableUsers();
        return availableUsers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(availableUsers);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<Locations>> getLocations() {
        List<Locations> locations = meetingService.getLocations();
        return locations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(locations);
    }

    // Obtener los asistentes a una reunión
    @GetMapping("/{meetingId}/attendees")
    public List<UUID> getAttendees(@PathVariable("meetingId") UUID meetingId) {
        Meetings meeting = meetingService.getMeetingById(meetingId)
                .orElseThrow(() -> new RuntimeException("ERROR: Reunión no encontrada"));
        return meetingService.getAttendeesForMeeting(meeting);
    }

    // Editar una reunión
    @PostMapping("/{meetingId}/modify")
    public ResponseEntity<String> editMeeting(@PathVariable("meetingId") UUID meetingId,
            @RequestBody Meetings changeMeeting) {

        meetingService.modifyMeeting(meetingId, changeMeeting);
        return ResponseEntity.status(HttpStatus.OK).body("Reunión modificada correctamente");
    }

    //Organizador cancela reunión
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Void> cancelMeeting(@PathVariable UUID meetingId) {
        try {
            meetingService.cancelMeetingByOrganizer(meetingId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    //Cancelar reunión si todas las invitaciones son rechazadas
    @PostMapping("/cancel/rejected")
    public ResponseEntity<String> cancelMeetingIfAllInvitationsRejected(
            @RequestParam UUID meetingId,
            @RequestParam UUID excludedUserId) {
        try {
            boolean result = meetingService.cancelMeetingIfAllInvitationsRejected(meetingId, excludedUserId);
            if (result) {
                return ResponseEntity.ok("Reunión cancelada debido a que todas las invitaciones fueron rechazadas");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La reunión sigue adelante, hay invitaciones pendientes o aceptadas");
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    
}

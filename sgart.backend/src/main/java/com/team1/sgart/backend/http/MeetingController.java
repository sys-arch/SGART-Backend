package com.team1.sgart.backend.http;

import com.team1.sgart.backend.model.Meeting;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.Invitation;
import com.team1.sgart.backend.model.InvitationStatus;
import com.team1.sgart.backend.services.MeetingService;
import com.team1.sgart.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserService userService;

    // Crear una reunión
    @PostMapping("/create")
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) {
        Meeting createdMeeting = meetingService.createMeeting(
                meeting.getTitle(),
                meeting.isAllDay(),
                meeting.getStartTime(),
                meeting.getEndTime(),
                meeting.getOrganizer(),
                meeting.getLocation(),
                meeting.getObservations()
        );
        return ResponseEntity.ok(createdMeeting);
    }

    // Obtener usuarios habilitados para ser invitados
    @GetMapping("/available-users")
    public ResponseEntity<List<User>> getAvailableUsers() {
        List<User> availableUsers = meetingService.getAvailableUsers();
        return availableUsers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(availableUsers);
    }

    /*
    // Comprobar la disponibilidad de un usuario para una reunión
    @GetMapping("/check-availability/{userId}")
    public ResponseEntity<Boolean> checkUserAvailability(@PathVariable UUID userId, @RequestParam("startTime") LocalTime startTime, 
    		@RequestParam("endTime") LocalTime endTime) {
        Optional<User> userOpt = userService.getUserById(userId);  // Obtener el usuario con el UserService para no hacer otro método
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean isAvailable = meetingService.isUserAvailable(user, startTime, endTime);
            return ResponseEntity.ok(isAvailable);
        } else {
            return ResponseEntity.notFound().build(); // Si el usuario no se encuentra
        }
    }
*/

    // Invitar a un usuario a una reunión
    @PostMapping("/invite/{meetingId}/{userId}")
    public ResponseEntity<Invitation> inviteUserToMeeting(@PathVariable UUID meetingId, @PathVariable UUID userId) {
        Optional<Meeting> meetingOpt = meetingService.getMeetingById(meetingId);
        Optional<User> userOpt = userService.getUserById(userId);
        
        if (meetingOpt.isPresent() && userOpt.isPresent()) {
            Meeting meeting = meetingOpt.get();
            User user = userOpt.get();
            Invitation invitation = meetingService.inviteUserToMeeting(meeting, user, InvitationStatus.PENDIENTE);
            return ResponseEntity.ok(invitation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}


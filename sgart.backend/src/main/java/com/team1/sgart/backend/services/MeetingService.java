package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.UserDao;

import com.team1.sgart.backend.dao.InvitationDAO;

import com.team1.sgart.backend.dao.MeetingDAO;
import com.team1.sgart.backend.model.Meeting;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.Invitation;
import com.team1.sgart.backend.model.InvitationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeetingService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MeetingDAO meetingDao;

    @Autowired
    private InvitationDAO invitationDao;

    // Método para crear la reunión
    public Meeting createMeeting(String title, boolean allDay, Time startTime, Time endTime, User organizer, String location, String observations) {
    	Meeting meeting = new Meeting(title, allDay, startTime, endTime, organizer, location, observations);
        meetingDao.save(meeting);
        return meeting;
    }

    // Método para obtener todos los usuarios habilitados
    public List<User> getAvailableUsers() {
        return userDao.findAllNotBlocked();  // Solo los usuarios no bloqueados
    }

    // Método para comprobar la disponibilidad de un usuario
    public boolean isUserAvailable(User user, LocalTime startTime, LocalTime endTime) {
        List<Invitation> invitations = userDao.checkUserAvailability(user, startTime, endTime);
        return invitations.isEmpty();  // Si no hay invitaciones que se solapen, está disponible
    }

    // Método para invitar a un usuario a una reunión
    public Invitation inviteUserToMeeting(Meeting meeting, User user, InvitationStatus status) {
        Invitation invitation = new Invitation(meeting, user, status, false, null);
        return invitationDao.save(invitation);
    }
    // Obtener una reunión por su ID
    public Optional<Meeting> getMeetingById(UUID meetingId) {
        return meetingDao.getMeetingById(meetingId);
    }

}

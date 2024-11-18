package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.UserDao;

import com.team1.sgart.backend.dao.InvitationDAO;

import com.team1.sgart.backend.dao.MeetingDAO;
import com.team1.sgart.backend.model.Meeting;
import com.team1.sgart.backend.model.User;

import jakarta.persistence.EntityNotFoundException;

import com.team1.sgart.backend.model.Invitation;
import com.team1.sgart.backend.model.InvitationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private UserDao userDao;
    private MeetingDAO meetingDao;   
    private InvitationDAO invitationDao;
    
    @Autowired
	public MeetingService(UserDao userDao, MeetingDAO meetingDao, InvitationDAO invitationDao) {
		this.userDao = userDao;
		this.meetingDao = meetingDao;
		this.invitationDao = invitationDao;
	}

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

    /* NO SE USA
    // Método para comprobar la disponibilidad de un usuario							
    public boolean isUserAvailable(User user, LocalTime startTime, LocalTime endTime) {
        List<Invitation> invitations = userDao.checkUserAvailability(user, startTime, endTime);
        return invitations.isEmpty();  // Si no hay invitaciones que se solapen, está disponible
    }
*/
    // Método para invitar a un usuario a una reunión
    public Invitation inviteUserToMeeting(Meeting meeting, User user, InvitationStatus status) {
        Invitation invitation = new Invitation(meeting, user, status, false, null);
        return invitationDao.save(invitation);
    }
    // Obtener una reunión por su ID
    public Optional<Meeting> getMeetingById(UUID meetingId) {
        return meetingDao.findById(meetingId);
    }
    // Obtetener asistentes de una reunión id
    public List<User> getAttendeesForMeeting(Meeting meeting) {
    List<Invitation> invitations = invitationDao.findByMeeting(meeting);
    
    // Filtramos aquellas las ACEPTADAS y devuelve los usuarios en una lista
    return invitations.stream()
            .filter(invitation -> invitation.getStatus() == InvitationStatus.ACEPTADA)
            .map(Invitation::getUser)
            .collect(Collectors.toList());
    }
    
    //Modificamos las reuniones
	public void modifyMeeting(UUID idMeeting, Meeting updatedMeeting) {
		Meeting meeting;
		//Comprobamos si la reunión existe
		if (!meetingDao.findById(idMeeting).isPresent()) {
			throw new EntityNotFoundException("Meeting " + idMeeting + " no encontrado");
		}
		
		else {
			 meeting = meetingDao.findById(idMeeting).get();
		}
				
		//Se revisan las reuniones del organizador, si tiene una reunión en el nuevo tramo, error de no permitido
		List<Meeting> conflictingMeetings = meetingDao.findConflictingMeetings(updatedMeeting.getTitle(),
				updatedMeeting.getStartTime(), updatedMeeting.getEndTime(), updatedMeeting.getLocation(),
				updatedMeeting.getOrganizer());
		if (!conflictingMeetings.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El organizador tiene una reunión en el nuevo tramo");
		}
		else 
		{
			// Se revisan las reuniones de los invitados, si tienen una reunión en el nuevo tramo, se elimina al invitado de la reunión
			List<User> attendees = getAttendeesForMeeting(meeting);
			for (User attendee : attendees) {
				conflictingMeetings = meetingDao.findConflictingMeetings(updatedMeeting.getTitle(),
						updatedMeeting.getStartTime(), updatedMeeting.getEndTime(), updatedMeeting.getLocation(),
						attendee);
				if (!conflictingMeetings.isEmpty()) {
					//Eliminar al invitado de la tabla de reunioines
				}
			}
		}
		
		meetingDao.updateMeeting(meeting, updatedMeeting);
	}
}

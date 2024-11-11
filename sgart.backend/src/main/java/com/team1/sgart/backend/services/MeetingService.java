package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.MeetingDAO;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.dao.DefaultWorkingHoursDAO;
import com.team1.sgart.backend.model.Meeting;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.DefaultWorkingHours;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {

    @Autowired
    private MeetingDAO meetingDAO;

    @Autowired
    private UserDao userDAO;

    @Autowired
    private DefaultWorkingHoursDAO workingHoursDAO;
    
    @Transactional
    public Meeting createMeeting(String title, LocalDateTime date, boolean allDay, 
                                 Time startTime, Time endTime, List<UUID> attendeeIds,
                                 String location, String notes) {
        
        if (!isValidWorkingHours(allDay, startTime, endTime)) {
            throw new IllegalArgumentException("La reunión no cumple con el horario laboral establecido.");
        }

        Meeting meeting = new Meeting();
        meeting.setTitle(title);
        meeting.setAllDay(allDay);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setLocation(location);
        meeting.setObservations(notes);

        User organizer = userDAO.findCurrentUser(organizer.getEmail());
        meeting.setOrganizer(organizer);

        List<User> attendees = userDAO.findAllById(attendeeIds);
        meeting.setAttendees(attendees);

        return meetingDAO.save(meeting); //falta
    }

    private boolean isValidWorkingHours(boolean allDay, Time startTime, Time endTime) {
        DefaultWorkingHours workingHours = workingHoursDAO.findWorkingHours();

        Time workingStartTime = Time.valueOf(workingHours.getStartTime());//falta
        Time workingEndTime = Time.valueOf(workingHours.getEndTime()); //falta

        if (allDay) {
            return startTime.equals(workingStartTime) && endTime.equals(workingEndTime);
        } else {
            return !startTime.before(workingStartTime) &&
                   !endTime.after(workingEndTime) &&
                   startTime.before(endTime);
        }
    }
}

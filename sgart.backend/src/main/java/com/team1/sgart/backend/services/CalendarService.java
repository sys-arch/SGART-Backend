package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.InvitationsDao;
import com.team1.sgart.backend.dao.MeetingsDao;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.InvitationsDTO;
import com.team1.sgart.backend.model.Meetings;
import com.team1.sgart.backend.model.MeetingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private final MeetingsDao meetingsDao;
    private final InvitationsDao invitationsDao;
    private final LocationsService locationsService;
    private final UserDao userDao;

    @Autowired
    public CalendarService(MeetingsDao meetingsDao, InvitationsDao invitationsDao, LocationsService locationsService, UserDao userDao) {
        this.meetingsDao = meetingsDao;
        this.invitationsDao = invitationsDao;
        this.locationsService = locationsService;
        this.userDao = userDao;
        logger.info("[!] CalendarService created");
    }
    public List<MeetingsDTO> loadMeetings() {
        List<Meetings> meetings = meetingsDao.findAll();
    
        return meetings.stream().map(meeting -> {
            MeetingsDTO meetingsDTO = new MeetingsDTO();
            meetingsDTO.setMeetingId(meeting.getMeetingId());
            meetingsDTO.setMeetingTitle(meeting.getMeetingTitle());
            meetingsDTO.setMeetingAllDay(meeting.isMeetingAllDay());
            meetingsDTO.setMeetingStartTime(meeting.getMeetingStartTime());
            meetingsDTO.setMeetingEndTime(meeting.getMeetingEndTime());
            meetingsDTO.setMeetingDate(meeting.getMeetingDate());
            meetingsDTO.setMeetingObservations(meeting.getMeetingObservations());
    
            // Obtener el nombre completo del organizador
            String organizerFullName = userDao.findUserFullNameById(meeting.getOrganizerId());
            meetingsDTO.setOrganizerName(organizerFullName); // Cambiar el atributo en el DTO
    
            String locationName = locationsService.getLocationById(meeting.getLocationId());
            meetingsDTO.setLocationName(locationName);
    
            return meetingsDTO;
        }).collect(Collectors.toList());
    }

    public List<InvitationsDTO> getDetailedInvitationsByMeetingId(UUID meetingId) {
        List<Object[]> results = invitationsDao.findDetailedInvitationsByMeetingId(meetingId);

        return results.stream().map(record -> {
            InvitationsDTO dto = new InvitationsDTO();
            try {
                dto.setInvitationId(UUID.fromString(record[0].toString())); // invitation_id
                dto.setMeetingId(UUID.fromString(record[1].toString()));    // meeting_id
                dto.setUserId(UUID.fromString(record[2].toString()));       // user_id
                dto.setUserName(record[3] + " " + record[4]);               // user_name y user_lastName
                dto.setInvitationStatus((String) record[5]);                // invitation_status
                dto.setUserAttendance((Boolean) record[6]);                 // user_attendance
                dto.setRejectionReason((String) record[7]);                 // rejection_reason
            } catch (Exception e) {
                logger.error("Error al mapear resultado a InvitationsDTO: ", e);
            }
            return dto;
        }).collect(Collectors.toList());
    }


    public List<MeetingsDTO> getMeetingsByUserId(UUID userId) {
        List<UUID> meetingIds = getMeetingIdsByUserId(userId);
        List<Meetings> meetings = getMeetingsByIds(meetingIds);
        return convertMeetingsToDTO(meetings);
    }

    private List<UUID> getMeetingIdsByUserId(UUID userId) {
        return invitationsDao.findByUserId(userId).stream()
                .map(invitation -> invitation.getMeeting().getMeetingId())
                .collect(Collectors.toList());
    }
    
    private List<Meetings> getMeetingsByIds(List<UUID> meetingIds) {
        return meetingsDao.findAllById(meetingIds);
    }

    private List<MeetingsDTO> convertMeetingsToDTO(List<Meetings> meetings) {
        return meetings.stream().map(meeting -> {
            MeetingsDTO meetingsDTO = new MeetingsDTO();
            meetingsDTO.setMeetingId(meeting.getMeetingId());
            meetingsDTO.setMeetingTitle(meeting.getMeetingTitle());
            meetingsDTO.setMeetingAllDay(meeting.isMeetingAllDay());
            meetingsDTO.setMeetingStartTime(meeting.getMeetingStartTime());
            meetingsDTO.setMeetingEndTime(meeting.getMeetingEndTime());
            meetingsDTO.setOrganizerId(meeting.getOrganizerId());
            meetingsDTO.setMeetingDate(meeting.getMeetingDate());
            meetingsDTO.setMeetingObservations(meeting.getMeetingObservations());
    

            String locationName = locationsService.getLocationById(meeting.getLocationId());
            meetingsDTO.setLocationName(locationName);
    
            return meetingsDTO;
        }).collect(Collectors.toList());
    }
    
    public boolean isUserAuthorizedForMeeting(UUID userId, UUID meetingId) {
        return invitationsDao.existsByUserIdAndMeetingId(userId, meetingId);
    }
    
    public List<MeetingsDTO> getOrganizedMeetingsByUserId(UUID userId) {
        List<Meetings> meetings = meetingsDao.findByOrganizerId(userId);
        
        return meetings.stream().map(meeting -> {
            MeetingsDTO meetingsDTO = new MeetingsDTO();
            meetingsDTO.setMeetingId(meeting.getMeetingId());
            meetingsDTO.setMeetingTitle(meeting.getMeetingTitle());
            meetingsDTO.setMeetingAllDay(meeting.isMeetingAllDay());
            meetingsDTO.setMeetingStartTime(meeting.getMeetingStartTime());
            meetingsDTO.setMeetingEndTime(meeting.getMeetingEndTime());
            meetingsDTO.setMeetingDate(meeting.getMeetingDate());
            meetingsDTO.setMeetingObservations(meeting.getMeetingObservations());
            
            // Obtener el nombre completo del organizador
            String organizerFullName = userDao.findUserFullNameById(meeting.getOrganizerId());
            meetingsDTO.setOrganizerName(organizerFullName);
            
            // Obtener el nombre de la ubicación
            String locationName = locationsService.getLocationById(meeting.getLocationId());
            meetingsDTO.setLocationName(locationName);
            
            return meetingsDTO;
        }).collect(Collectors.toList());
    }
    
}

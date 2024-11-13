package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.InvitationsDao;
import com.team1.sgart.backend.dao.MeetingsDao;
import com.team1.sgart.backend.model.Invitations;
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

    @Autowired
    public CalendarService(MeetingsDao meetingsDao, InvitationsDao invitationsDao) {
        this.meetingsDao = meetingsDao;
        this.invitationsDao = invitationsDao;
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
            meetingsDTO.setOrganizerId(meeting.getOrganizerId());
            meetingsDTO.setMeetingDate(meeting.getMeetingDate());
            return meetingsDTO;
        }).collect(Collectors.toList());
    }
    

    public List<InvitationsDTO> getDetailedInvitationsByMeetingId(UUID meetingId) {
        List<Object[]> results = invitationsDao.findDetailedInvitationsByMeetingId(meetingId);
    
        return results.stream().map(record -> {
            InvitationsDTO dto = new InvitationsDTO();
            dto.setInvitationId((Integer) record[0]);
            dto.setMeetingId(UUID.fromString((String) record[1]));
            dto.setUserId(UUID.fromString((String) record[2])); // Conversión de String a UUID
            dto.setUserName(record[3] + " " + record[4]); // Combinar user_name y user_lastName
            dto.setInvitationStatus((String) record[5]);
            dto.setUserAttendance((Boolean) record[6]);
            dto.setRejectionReason((String) record[7]);
            return dto;
        }).collect(Collectors.toList());
    }
    
}

package com.team1.sgart.backend.services;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.dao.InvitationsDao;
import com.team1.sgart.backend.dao.MeetingsDao;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.InvitationStatus;
import com.team1.sgart.backend.model.Invitations;
import com.team1.sgart.backend.model.Meetings;
import com.team1.sgart.backend.model.User;

class MeetingServiceTest {

    private static final String MEETING_TITLE = "Team Meeting";
    private static final String EXAMPLE_HOUR = "10:00";

    @Mock
    private MeetingsDao meetingDao;

    @Mock
    private UserDao userDao;

    @Mock
    private InvitationsDao invitationsDao;

    @InjectMocks
    private MeetingService meetingService;

    private Meetings existingMeeting;
    private Meetings updatedMeeting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingMeeting = new Meetings("Initial Meeting", LocalDate.now().plusDays(1), false, LocalTime.of(9, 0),
                LocalTime.of(10, 0), "Initial Observations", UUID.randomUUID(), UUID.randomUUID());
        existingMeeting.setMeetingId(UUID.randomUUID());

        updatedMeeting = new Meetings("Updated Meeting", LocalDate.now().plusDays(2), false, LocalTime.of(10, 0),
                LocalTime.of(11, 0), "Updated Observations", existingMeeting.getOrganizerId(),
                existingMeeting.getLocationId());
    }

    @Test
    void createMeeting_ShouldSaveAndReturnMeeting() {
        UUID organizerId = UUID.randomUUID();
        Meetings meeting = new Meetings(MEETING_TITLE, LocalDate.parse("2021-12-01"), false, LocalTime.parse(EXAMPLE_HOUR),
                LocalTime.parse("11:00"), "Monthly sync", organizerId, UUID.randomUUID());

        when(meetingDao.save(any(Meetings.class))).thenReturn(meeting);

        Meetings createdMeeting = meetingService.createMeeting(MEETING_TITLE, false, LocalDate.parse("2021-12-01"),
                LocalTime.parse(EXAMPLE_HOUR), LocalTime.parse("11:00"), "Monthly sync", organizerId, UUID.randomUUID());

        assertNotNull(createdMeeting);
        assertEquals(MEETING_TITLE, createdMeeting.getMeetingTitle());
        verify(meetingDao, times(1)).save(any(Meetings.class));
    }

    @Test
    void getAvailableUsers_ShouldReturnNonBlockedUsers() {
        User user1 = new User("user1@example.com", "User", "One", null, null, null, null, null, null, false, false, null);
        User user2 = new User("user2@example.com", "User", "Two", null, null, null, null, null, null, false, false, null);

        when(userDao.findAllNonAdminAndNotBlocked()).thenReturn(List.of(user1, user2));

        List<User> availableUsers = meetingService.getAvailableUsers();

        assertNotNull(availableUsers);
        assertEquals(2, availableUsers.size());
        verify(userDao, times(1)).findAllNonAdminAndNotBlocked();
    }

    @Test
    void inviteUserToMeeting_ShouldSaveAndReturnInvitation() {
        User user = new User("user@example.com", "User", "Test", null, null, null, null, null, null, false, false, null);
        Meetings meeting = new Meetings("Project Kickoff", LocalDate.parse("2024-12-15"), false,
                LocalTime.parse("09:00"), LocalTime.parse("10:00"), "", user.getID(), UUID.randomUUID());
        Invitations invitation = new Invitations(meeting, user, InvitationStatus.PENDIENTE.name(), false, "");

        when(invitationsDao.save(any(Invitations.class))).thenReturn(invitation);

        Invitations createdInvitation = meetingService.inviteUserToMeeting(meeting, user, InvitationStatus.PENDIENTE);

        assertNotNull(createdInvitation);
        assertEquals(InvitationStatus.PENDIENTE.name(), createdInvitation.getInvitationStatus());
        assertEquals(meeting, createdInvitation.getMeeting());
        verify(invitationsDao, times(1)).save(any(Invitations.class));
    }

    @Test
    void getMeetingById_ShouldReturnMeetingIfExists() {
        UUID meetingId = UUID.randomUUID();
        Meetings meeting = new Meetings();
        meeting.setMeetingId(meetingId);

        when(meetingDao.findById(meetingId)).thenReturn(Optional.of(meeting));

        Optional<Meetings> result = meetingService.getMeetingById(meetingId);

        assertTrue(result.isPresent());
        assertEquals(meetingId, result.get().getMeetingId());
        verify(meetingDao, times(1)).findById(meetingId);
    }

    @Test
    void getAttendeesForMeeting_ShouldReturnAcceptedUsers() {
        User user1 = new User();
        User user2 = new User();
        user1.setID(UUID.randomUUID());
        user2.setID(UUID.randomUUID());

        Invitations invitation1 = new Invitations(existingMeeting, user1, InvitationStatus.ACEPTADA.name(), false, "");
        Invitations invitation2 = new Invitations(existingMeeting, user2, InvitationStatus.ACEPTADA.name(), false, "");

        when(invitationsDao.findByMeetingId(existingMeeting.getMeetingId())).thenReturn(List.of(invitation1, invitation2));

        List<UUID> attendees = meetingService.getAttendeesForMeeting(existingMeeting);

        assertEquals(2, attendees.size());
        assertTrue(attendees.contains(user1.getID()));
        assertTrue(attendees.contains(user2.getID()));
        verify(invitationsDao, times(1)).findByMeetingId(existingMeeting.getMeetingId());
    }

    @Test
    void testModifyMeetingSuccess() {
        when(meetingDao.findById(existingMeeting.getMeetingId())).thenReturn(Optional.of(existingMeeting));
        when(meetingDao.findConflictingMeetings(updatedMeeting.getMeetingDate(), updatedMeeting.getMeetingStartTime(),
                updatedMeeting.getMeetingEndTime())).thenReturn(Collections.emptyList());

        meetingService.modifyMeeting(existingMeeting.getMeetingId(), updatedMeeting);

        verify(meetingDao, times(1)).updateMeeting(existingMeeting, updatedMeeting);
    }

    @Test
    void testModifyMeetingMeetingNotFound() {
        when(meetingDao.findById(existingMeeting.getMeetingId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            meetingService.modifyMeeting(existingMeeting.getMeetingId(), updatedMeeting);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(meetingDao, never()).updateMeeting(any(), any());
    }

    @Test
    void testCancelMeetingByOrganizer_Success() {
        UUID meetingId = UUID.randomUUID();
        Meetings meeting = new Meetings();
        meeting.setMeetingId(meetingId);

        when(meetingDao.findById(meetingId)).thenReturn(Optional.of(meeting));

        boolean result = meetingService.cancelMeetingByOrganizer(meetingId);

        assertTrue(result);
        verify(meetingDao, times(1)).deleteInvitationsByMeetingId(meetingId);
        verify(meetingDao, times(1)).deleteMeetingById(meetingId);
    }

    @Test
    void testCancelMeetingByOrganizer_MeetingNotFound() {
        UUID meetingId = UUID.randomUUID();

        when(meetingDao.findById(meetingId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            meetingService.cancelMeetingByOrganizer(meetingId);
        });

        assertEquals("Reunión no encontrada", exception.getMessage());
        verify(meetingDao, never()).delete(any(Meetings.class));
    }
}

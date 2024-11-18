package com.team1.sgart.backend.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.team1.sgart.backend.model.Invitations;

@Repository
public interface InvitationsDao extends JpaRepository<Invitations, Integer> {
    @Query(value = """
        SELECT i.invitation_id, i.meeting_id, i.user_id, u.user_name, u.user_lastName, 
               i.invitation_status, i.user_attendance, i.rejection_reason
        FROM SGART_InvitationsTable i
        JOIN SGART_UsersTable u ON i.user_id = u.user_id
        WHERE i.meeting_id = :meetingId
        """, nativeQuery = true)
    List<Object[]> findDetailedInvitationsByMeetingId(@Param("meetingId") UUID meetingId);

    @Query("SELECT i FROM Invitations i WHERE i.meeting.meetingId = :meetingId")
    List<Invitations> findByMeetingId(@Param("meetingId") UUID meetingId);

}
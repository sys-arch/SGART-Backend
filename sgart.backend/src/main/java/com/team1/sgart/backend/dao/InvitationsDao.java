package com.team1.sgart.backend.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.team1.sgart.backend.model.Invitations;

import jakarta.transaction.Transactional;

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

    @Query("SELECT i FROM Invitations i WHERE i.user.id = :userId")
    List<Invitations> findByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT COUNT(*) FROM SGART_InvitationsTable " +
           "WHERE MEETING_ID = :meetingId AND USER_ID = :userId", 
           nativeQuery = true)
    int countByMeetingIdAndUserId(
        @Param("meetingId") UUID meetingId, 
        @Param("userId") UUID userId
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE SGART_InvitationsTable " +
           "SET INVITATION_STATUS = :newStatus, REJECTION_REASON = :comment " +
           "WHERE MEETING_ID = :meetingId AND USER_ID = :userId", 
           nativeQuery = true)
    int updateInvitationStatus(
        @Param("meetingId") UUID meetingId, 
        @Param("userId") UUID userId, 
        @Param("newStatus") String newStatus,
        @Param("comment") String comment
    );


    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
           "FROM SGART_InvitationsTable " +
           "WHERE MEETING_ID = :meetingId AND USER_ID = :userId", 
           nativeQuery = true)
    boolean existsByUserIdAndMeetingId(
        @Param("userId") UUID userId, 
        @Param("meetingId") UUID meetingId
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE SGART_InvitationsTable " +
           "SET USER_ATTENDANCE = 1 " +
           "WHERE MEETING_ID = :meetingId AND USER_ID = :userId", 
           nativeQuery = true)
    int updateUserAttendance(
        @Param("meetingId") UUID meetingId, 
        @Param("userId") UUID userId
    );

    @Query(value = "SELECT CASE WHEN USER_ATTENDANCE = 1 THEN 1 ELSE 0 END " +
           "FROM SGART_InvitationsTable " +
           "WHERE MEETING_ID = :meetingId AND USER_ID = :userId", 
           nativeQuery = true)
    Integer getUserAttendance(
        @Param("meetingId") UUID meetingId, 
        @Param("userId") UUID userId
    );
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SGART_InvitationsTable WHERE user_id = :userId", nativeQuery = true)
    void deleteByUser(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SGART_InvitationsTable WHERE meeting_id = :meetingId AND user_id = :userId", nativeQuery = true)
    void deleteByMeetingIdAndUserId(@Param("meetingId") UUID meetingId, @Param("userId") UUID userId);
    
  	@Query(value = "SELECT COUNT(*) FROM SGART_InvitationsTable "
			+ "WHERE meeting_id = :meetingId AND user_id = :userId", nativeQuery = true)
  	int checkUserHaveMeeting(@Param("meetingId") UUID meetingId, @Param("userId") UUID userId);
	
	  @Query(value = "SELECT user_id FROM SGART_InvitationsTable WHERE meeting_id = :meetingId", nativeQuery = true)
	  List<UUID> findUserIdsByMeetingId(@Param("meetingId") UUID meetingId);

    @Modifying
    @Query(value = """
            INSERT INTO SGART_InvitationsTable 
            (MEETING_ID, USER_ID, INVITATION_STATUS, REJECTION_REASON, USER_ATTENDANCE) 
            VALUES 
            (:meetingId, :userId, :status, :comment, 0)
            """, nativeQuery = true)
    int createInvitation(
        @Param("meetingId") UUID meetingId,
        @Param("userId") UUID userId,
        @Param("status") String status,
        @Param("comment") String comment
    );

}
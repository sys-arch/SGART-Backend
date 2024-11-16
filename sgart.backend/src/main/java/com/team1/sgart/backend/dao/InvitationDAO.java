package com.team1.sgart.backend.dao;

import com.team1.sgart.backend.model.Invitation;
import com.team1.sgart.backend.model.InvitationStatus;
import com.team1.sgart.backend.model.Meeting;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationDAO extends JpaRepository<Invitation, Integer> {

	// Método para obtener todas las invitaciones de una reunión
	List<Invitation> findByMeeting(Meeting meeting);
	

}
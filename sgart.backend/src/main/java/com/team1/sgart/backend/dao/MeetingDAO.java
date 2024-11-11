package com.team1.sgart.backend.dao;


import com.team1.sgart.backend.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeetingDAO extends JpaRepository<Meeting, UUID> {

    // Método para obtener una reunión por su ID
    Optional<Meeting> getMeetingById(UUID meetingId);
}

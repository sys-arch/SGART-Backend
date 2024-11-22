package com.team1.sgart.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.team1.sgart.backend.model.Meetings;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeetingsDao extends JpaRepository<Meetings, UUID> {
    // Método para obtener una reunión por su ID
    Optional<Meetings> findById(UUID meetingId);

    @Query("SELECT m FROM Meetings m WHERE m.organizerId = :userId")
    List<Meetings> findByOrganizerId(@Param("userId") UUID userId);
}

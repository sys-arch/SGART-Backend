package com.team1.sgart.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.model.Absences;

import java.util.List;
import java.util.UUID;

@Repository
public interface AbsencesDao extends JpaRepository<Absences, UUID> {
    List<Absences> findByUserId(UUID userId);
}

package com.team1.sgart.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.team1.sgart.backend.model.Meetings;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingsDao extends JpaRepository<Meetings, UUID> {
}

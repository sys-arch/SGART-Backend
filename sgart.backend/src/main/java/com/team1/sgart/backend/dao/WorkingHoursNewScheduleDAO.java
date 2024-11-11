package com.team1.sgart.backend.dao;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.model.WorkingHoursNewSchedule;

@Repository
public interface WorkingHoursNewScheduleDAO extends JpaRepository<WorkingHoursNewSchedule, UUID> {
}


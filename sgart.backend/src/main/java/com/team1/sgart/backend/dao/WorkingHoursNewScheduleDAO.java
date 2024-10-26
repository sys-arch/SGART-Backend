package com.team1.sgart.backend.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.modules.WorkingHoursNewSchedule;

@Repository
public interface WorkingHoursNewScheduleDAO extends JpaRepository<WorkingHoursNewSchedule, UUID> {
    
}


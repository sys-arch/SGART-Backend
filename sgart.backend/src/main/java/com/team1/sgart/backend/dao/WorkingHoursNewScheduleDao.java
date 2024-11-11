package com.team1.sgart.backend.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.model.WorkingHoursNewSchedule;

@Repository
public interface WorkingHoursNewScheduleDao extends JpaRepository<WorkingHoursNewSchedule, UUID> {
    //PARA EVENTOS NO USAR
}


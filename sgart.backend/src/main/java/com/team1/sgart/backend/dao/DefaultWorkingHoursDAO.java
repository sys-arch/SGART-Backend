package com.team1.sgart.backend.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.model.DefaultWorkingHours;

@Repository
public interface DefaultWorkingHoursDAO extends JpaRepository<DefaultWorkingHours, Integer> { 
    @Query("SELECT w FROM WorkingHours w")
    DefaultWorkingHours findWorkingHours();
}

/* la tabla es esta:
 * CREATE TABLE SGART_WorkingHours (
    id INT IDENTITY(1,1) PRIMARY KEY, 
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);
*/

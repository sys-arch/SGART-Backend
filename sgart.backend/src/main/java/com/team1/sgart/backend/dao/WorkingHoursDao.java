package com.team1.sgart.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.model.WorkingHours;

@Repository
public interface WorkingHoursDao extends JpaRepository<WorkingHours, Integer> {
	
	// Método para obtener el horario laboral
	WorkingHours findWorkingHours();

}


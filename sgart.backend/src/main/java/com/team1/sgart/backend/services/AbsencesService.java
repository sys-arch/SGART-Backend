package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.AbsencesDao;
import com.team1.sgart.backend.model.Absences;
import com.team1.sgart.backend.model.AbsencesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AbsencesService {

    private static final Logger logger = LoggerFactory.getLogger(AbsencesService.class);
    private final AbsencesDao absencesDao;

    @Autowired
    public AbsencesService(AbsencesDao absencesDao) {
        this.absencesDao = absencesDao;
        logger.info("[!] AbsencesService created");
    }
    
    public List<AbsencesDTO> getAllAbsences() {
    	List<Absences> absencesList = absencesDao.findAll();

        return absencesList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
	}

    public List<AbsencesDTO> getAbsencesByUser(UUID userId) {
        List<Absences> absencesList = absencesDao.findByUserId(userId);

        return absencesList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AbsencesDTO createAbsence(AbsencesDTO absenceDto) {
        Absences absence = convertToEntity(absenceDto);
        Absences savedAbsence = absencesDao.save(absence);
        return convertToDto(savedAbsence);
    }

    private AbsencesDTO convertToDto(Absences absence) {
        AbsencesDTO dto = new AbsencesDTO();
        dto.setAbsenceId(absence.getAbsenceId());
        dto.setUserId(absence.getUserId());
        dto.setAbsenceStartDate(absence.getAbsenceStartDate());
        dto.setAbsenceEndDate(absence.getAbsenceEndDate());
        dto.setAbsenceAllDay(absence.isAbsenceAllDay());
        dto.setAbsenceStartTime(absence.getAbsenceStartTime());
        dto.setAbsenceEndTime(absence.getAbsenceEndTime());
        dto.setAbsenceReason(absence.getAbsenceReason());
        return dto;
    }

    private Absences convertToEntity(AbsencesDTO absenceDto) {
        Absences absence = new Absences();
        absence.setUserId(absenceDto.getUserId());
        absence.setAbsenceStartDate(absenceDto.getAbsenceStartDate());
        absence.setAbsenceEndDate(absenceDto.getAbsenceEndDate());
        absence.setAbsenceAllDay(absenceDto.isAbsenceAllDay());
        absence.setAbsenceStartTime(absenceDto.getAbsenceStartTime());
        absence.setAbsenceEndTime(absenceDto.getAbsenceEndTime());
        absence.setAbsenceReason(absenceDto.getAbsenceReason());
        return absence;
    }
}
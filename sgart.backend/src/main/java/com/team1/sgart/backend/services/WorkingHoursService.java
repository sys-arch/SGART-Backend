package com.team1.sgart.backend.services;

import com.team1.sgart.backend.model.WorkingHoursDTO;
import com.team1.sgart.backend.dao.WorkingHoursDao;
import com.team1.sgart.backend.model.WorkingHours;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkingHoursService {
    private static final Logger logger = LoggerFactory.getLogger(WorkingHoursService.class);
    private WorkingHoursDao workingHoursDao;

    @Autowired
    public WorkingHoursService(WorkingHoursDao workingHoursDao){
        logger.info("[!] WorkingHoursService created");
        this.workingHoursDao = workingHoursDao;
    }

    public List<WorkingHoursDTO> getAllWorkingHours() {
        return workingHoursDao.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public void saveWorkingHoursList(List<WorkingHoursDTO> workingHoursDTOList) {
        List<WorkingHours> workingHoursList = workingHoursDTOList.stream()
            .map(dto -> new WorkingHours(dto.getStartTime(), dto.getEndTime()))
            .collect(Collectors.toList());

        workingHoursDao.saveAll(workingHoursList);
    }

    private WorkingHoursDTO convertToDTO(WorkingHours workingHours) {
        return new WorkingHoursDTO(
            workingHours.getId(),
            workingHours.getStartTime(),
            workingHours.getEndTime()
        );
    }
}

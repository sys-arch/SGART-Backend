package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.WorkingHoursDTO;
import com.team1.sgart.backend.services.WorkingHoursService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@RestController
@RequestMapping("/administrador/horarios")
public class AdminWorkingHoursController{

    private static final Logger logger = LoggerFactory.getLogger(AdminWorkingHoursController.class);
    private final WorkingHoursService workingHoursService;

    @Autowired
    public AdminWorkingHoursController(WorkingHoursService workingHoursService) {
        this.workingHoursService = workingHoursService;
        logger.info("[!] AdminWorkingHoursController created");
    }


    @GetMapping
    public ResponseEntity<List<WorkingHoursDTO>> getAllWorkingHours() {
        List<WorkingHoursDTO> workingHoursList = workingHoursService.getAllWorkingHours();
        return ResponseEntity.ok(workingHoursList); // Devuelve una lista vacía si no hay datos
    }

    @PostMapping
    public ResponseEntity<String> createWorkingHours(@RequestBody List<WorkingHoursDTO> workingHoursList) {
        if (workingHoursList.isEmpty()) {
            return ResponseEntity.badRequest().body("La lista de horarios no puede estar vacía.");
        }
        workingHoursService.saveWorkingHoursList(workingHoursList);
        return ResponseEntity.ok("Horarios de trabajo guardados correctamente.");
    }

}

package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team1.sgart.backend.model.AbsencesDTO;
import com.team1.sgart.backend.services.AbsencesService;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("administrador/ausencias")
@CrossOrigin("*")
public class AbsencesController {

    private static final Logger logger = LoggerFactory.getLogger(AbsencesController.class);
    private final AbsencesService absencesService;

    @Autowired
    public AbsencesController(AbsencesService absencesService) {
        this.absencesService = absencesService;
        logger.info("[!] AdminAbsencesController created");
    }
    
    @GetMapping("/loadAbsences")
    public ResponseEntity<List<AbsencesDTO>> getAllAbsenses() {
        List<AbsencesDTO> absencesList = absencesService.getAllAbsences();
        if (absencesList == null || absencesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(absencesList);
    }

    @GetMapping("/loadAbsences/{userId}")
    public ResponseEntity<List<AbsencesDTO>> getAbsencesByUser(@PathVariable UUID userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<AbsencesDTO> absencesList = absencesService.getAbsencesByUser(userId);
        if (absencesList == null || absencesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(absencesList);
    }


    @PostMapping("/newAbsence")
    public ResponseEntity<AbsencesDTO> createAbsence(@RequestBody AbsencesDTO absenceDto) {
        if (absenceDto == null) {
            return ResponseEntity.badRequest().build();
        }

        AbsencesDTO createdAbsence = absencesService.createAbsence(absenceDto);
        if (createdAbsence == null) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok(createdAbsence);
    }
}

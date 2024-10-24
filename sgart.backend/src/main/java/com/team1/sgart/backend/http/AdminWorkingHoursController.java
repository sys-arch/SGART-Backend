package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.team1.sgart.backend.services.CalendarEventService;
import com.team1.sgart.backend.modules.CalendarEventDTO;

@RestController
@RequestMapping("/administrador/eventos")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminWorkingHoursController{

    private static final Logger logger = LoggerFactory.getLogger(AdminWorkingHoursController.class);
    private CalendarEventService eventService;

    @Autowired
    private AdminWorkingHoursController(CalendarEventService eventService){
        logger.info("[!] AdminWorkingHoursController created");
        this.eventService = eventService;
    }

    /* Method to get an event from the Frontend and save it in the database */
    @PostMapping("/saveEvent")
    public ResponseEntity<Map<String, String>> saveEvent(@RequestBody CalendarEventDTO eventDTO){
        try {
            eventService.saveEvent(eventDTO);
            logger.info("[!] Evento guardado exitosamente!");
            Map<String, String> response = new HashMap<>();
            response.put("message", "Evento guardado correctamente");

            return ResponseEntity.status(HttpStatus.OK).body(response);  
        } catch (Exception e) {
            logger.error("[!] Problema al guardar el evento!", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al guardar el evento");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/loadEvents")
    public ResponseEntity<List<CalendarEventDTO>> loadEvents(){
        try {
            List<CalendarEventDTO> events = eventService.loadAllEvents();
            logger.info("[!] Los eventos se han cargado correctamente en el calendario.");
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            logger.error("[!] Error al obtener los eventos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

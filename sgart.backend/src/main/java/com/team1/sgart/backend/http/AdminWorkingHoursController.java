package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.team1.sgart.backend.modules.DefaultWorkingHoursDTO;
import com.team1.sgart.backend.modules.WorkingHoursNewScheduleDTO;

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

    /* Method to load the events from the database into the calendar */
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

    /* Method to save a day with different work schedule */
    @PostMapping("/saveDay")
    public  ResponseEntity<Map<String, String>> saveWorkingDayException(@RequestBody WorkingHoursNewScheduleDTO exceptionDTO){
        try {
            eventService.saveWorkingHourSchedule(exceptionDTO);
            logger.info("[!] Excepción guardada exitosamente!");
            Map<String, String> response = new HashMap<>();
            response.put("message", "Excepción guardada correctamente");

            return ResponseEntity.status(HttpStatus.OK).body(response);  
        } catch (Exception e) {
            logger.error("[!] Problema al guardar la excepción!", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al guardar la excepción");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

        /* Method to load the days with different schedule from the database into the calendar */
        @GetMapping("/loadSchedules")
        public ResponseEntity<List<WorkingHoursNewScheduleDTO>> loadScheduleExceptions(){
            try {
                List<WorkingHoursNewScheduleDTO> schedules = eventService.loadScheduleExceptions();
                logger.info("Horarios modificados enviados al frontend: {}", schedules);
                logger.info("[!] Las excepciones en los horarios se han cargado correctamente en el calendario.");
                return ResponseEntity.ok(schedules);
            } catch (Exception e) {
                logger.error("[!] Error al obtener las excepciones en los horarios", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        /* Method to load the default working hours from the data base into the calendar */
        @GetMapping("/loadDefaultSchedule")
        public ResponseEntity<List<DefaultWorkingHoursDTO>> loadDefaultWorkingHours(){
            try {
                List<DefaultWorkingHoursDTO> defaultSchedules = eventService.loadDefaultWorkingHours();
                logger.info("Horarios modificados enviados al frontend: {}", defaultSchedules);
                logger.info("[!] Los horarios por defecto se han cargado correctamente en el calendario.");
                return ResponseEntity.ok(defaultSchedules);
            } catch (Exception e) {
                logger.error("[!] Error al cargar los horarios por defecto", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
}

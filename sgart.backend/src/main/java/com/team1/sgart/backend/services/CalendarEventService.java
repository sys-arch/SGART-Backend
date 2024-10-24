package com.team1.sgart.backend.services;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team1.sgart.backend.dao.CalendarEventRepository;
import com.team1.sgart.backend.http.AdminWorkingHoursController;
import com.team1.sgart.backend.modules.CalendarEvent;
import com.team1.sgart.backend.modules.CalendarEventDTO;

@Service
public class CalendarEventService {

    private static final Logger logger = LoggerFactory.getLogger(AdminWorkingHoursController.class);
    private CalendarEventRepository eventRepository;

    @Autowired
    public CalendarEventService(CalendarEventRepository eventRepository) {
        logger.info("[!] CalendarEventService created");
        this.eventRepository = eventRepository;
    }

    public void saveEvent(CalendarEventDTO eventDTO) {
        // Log para ver qué está recibiendo el método
        logger.info("Recibiendo evento para guardar: ");
        logger.info("Título del evento: {}", eventDTO.getEventTitle());
        logger.info("Fecha de inicio: {}", eventDTO.getEventStartDate());
        logger.info("¿Todo el día?: {}", eventDTO.isEventAllDay());
        logger.info("Hora de inicio: {}", eventDTO.isEventAllDay() ? "00:00" : eventDTO.getEventTimeStart());
        logger.info("Hora de fin: {}", eventDTO.isEventAllDay() ? "23:59" : eventDTO.getEventTimeEnd());
        logger.info("Frecuencia del evento: {}", eventDTO.getEventFrequency());
        logger.info("Número de repeticiones: {}", eventDTO.getEventRepetitionsCount());

        CalendarEvent event = new CalendarEvent();
        event.setEventTitle(eventDTO.getEventTitle());
        event.setEventStartDate(eventDTO.getEventStartDate());
        event.setEventAllDay(eventDTO.isEventAllDay());
        event.setEventTimeStart(eventDTO.isEventAllDay() ? LocalTime.of(0, 0) : eventDTO.getEventTimeStart());
        event.setEventTimeEnd(eventDTO.isEventAllDay() ? LocalTime.of(23, 59) : eventDTO.getEventTimeEnd());
        event.setEventFrequency(eventDTO.getEventFrequency());
        event.setEventRepetitionsCount(eventDTO.getEventRepetitionsCount());

        eventRepository.save(event);
    }

    public List<CalendarEventDTO> loadEvents() {
        // Obtener todos los eventos
        List<CalendarEvent> events = eventRepository.findAll();

        // Convertirlos en el formato correcto para el front
        return events.stream().map(event -> {
            CalendarEventDTO dto = new CalendarEventDTO();
            dto.setEventTitle(event.getEventTitle());
            dto.setEventStartDate(event.getEventStartDate());
            dto.setEventAllDay(event.isEventAllDay());
            dto.setEventTimeStart(event.getEventTimeStart());
            dto.setEventTimeEnd(event.getEventTimeEnd());
            dto.setEventFrequency(event.getEventFrequency());
            dto.setEventRepetitionsCount(event.getEventRepetitionsCount());
            return dto;
        }).collect(Collectors.toList());
    }

}

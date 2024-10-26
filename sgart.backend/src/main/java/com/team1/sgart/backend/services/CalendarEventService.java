package com.team1.sgart.backend.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team1.sgart.backend.dao.CalendarEventDAO;
import com.team1.sgart.backend.dao.DefaultWorkingHoursDAO;
import com.team1.sgart.backend.dao.WorkingHoursNewScheduleDAO;
import com.team1.sgart.backend.modules.CalendarEvent;
import com.team1.sgart.backend.modules.CalendarEventDTO;
import com.team1.sgart.backend.modules.DefaultWorkingHours;
import com.team1.sgart.backend.modules.DefaultWorkingHoursDTO;
import com.team1.sgart.backend.modules.WorkingHoursNewSchedule;
import com.team1.sgart.backend.modules.WorkingHoursNewScheduleDTO;

@Service
public class CalendarEventService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarEventService.class);
    private CalendarEventDAO eventRepository;
    private WorkingHoursNewScheduleDAO newScheduleRepository;
    private DefaultWorkingHoursDAO defaultWorkingHoursDAO;

    @Autowired
    public CalendarEventService(CalendarEventDAO eventRepository, WorkingHoursNewScheduleDAO newScheduleRepository, DefaultWorkingHoursDAO defaultWorkingHoursDAO) {
        logger.info("[!] CalendarEventService created");
        this.eventRepository = eventRepository;
        this.newScheduleRepository = newScheduleRepository;
        this.defaultWorkingHoursDAO = defaultWorkingHoursDAO;
    }

    public void saveEvent(CalendarEventDTO eventDTO) {
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
        // Obtener todos los eventos de la base de datos
        List<CalendarEvent> events = eventRepository.findAll();

        // Convertirlos en el formato correcto para el frontend
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

    public List<CalendarEventDTO> loadAllEvents() {
        // Cargar todos los eventos y luego calcular las recurrencias
        List<CalendarEventDTO> baseEvents = loadEvents();
        return calculateRecurrences(baseEvents);
    }

    private List<CalendarEventDTO> calculateRecurrences(List<CalendarEventDTO> events) {
        List<CalendarEventDTO> allEvents = new ArrayList<>(events);

        for (CalendarEventDTO event : events) {
            if (event.getEventRepetitionsCount() != null
                    && (event.getEventRepetitionsCount() > 1 || event.getEventRepetitionsCount() == -1)) {
                // Calcular las recurrencias basadas en el tipo de frecuencia
                switch (event.getEventFrequency()) {
                    case "Diario":
                        generateRecurringEvents(event, allEvents, 1, ChronoUnit.DAYS);
                        break;
                    case "Semanal":
                        generateRecurringEvents(event, allEvents, 7, ChronoUnit.DAYS);
                        break;
                    case "Mensual":
                        generateRecurringEvents(event, allEvents, 1, ChronoUnit.MONTHS);
                        break;
                    case "Anual":
                        generateRecurringEvents(event, allEvents, 1, ChronoUnit.YEARS);
                        break;
                    default:
                        break; // No hacer nada si es "Una vez"
                }
            }
        }
        return allEvents;
    }

    private void generateRecurringEvents(CalendarEventDTO originalEvent, List<CalendarEventDTO> allEvents,
            int amountToAdd, TemporalUnit unit) {
        LocalDate startDate = originalEvent.getEventStartDate();

        // Si las repeticiones son ilimitadas (es decir, eventRepetitionsCount = -1),
        // establecemos un límite máximo (100 por ejemplo)
        int maxRepetitions = originalEvent.getEventRepetitionsCount() == -1 ? 100
                : originalEvent.getEventRepetitionsCount();

        for (int i = 2; i <= maxRepetitions; i++) {
            // Crear una copia del evento original para la nueva recurrencia
            CalendarEventDTO newEvent = new CalendarEventDTO();
            newEvent.setEventTitle(originalEvent.getEventTitle() + " " + i); // Añadir el contador al título
            newEvent.setEventAllDay(originalEvent.isEventAllDay());
            newEvent.setEventTimeStart(originalEvent.getEventTimeStart());
            newEvent.setEventTimeEnd(originalEvent.getEventTimeEnd());
            newEvent.setEventFrequency(originalEvent.getEventFrequency());
            newEvent.setEventRepetitionsCount(originalEvent.getEventRepetitionsCount());

            // Calcular la nueva fecha basándose en la frecuencia (diaria, semanal, etc.)
            LocalDate newStartDate = startDate.plus((i - 1) * amountToAdd, unit);
            newEvent.setEventStartDate(newStartDate);

            // Añadir el nuevo evento a la lista de eventos
            allEvents.add(newEvent);
        }
    }

    public void saveWorkingHourSchedule(WorkingHoursNewScheduleDTO exceptionDTO) {
        WorkingHoursNewSchedule newSchedule = new WorkingHoursNewSchedule();
        newSchedule.setExceptionDate(exceptionDTO.getExceptionDate());
        newSchedule.setStartTime(exceptionDTO.getStartTime());
        newSchedule.setEndTime(exceptionDTO.getEndTime());
        newSchedule.setReason(exceptionDTO.getReason());
        newScheduleRepository.save(newSchedule);
    }

    public List<WorkingHoursNewScheduleDTO> loadScheduleExceptions() {
        // Obtener todos los eventos de la base de datos
        List<WorkingHoursNewSchedule> schedules = newScheduleRepository.findAll();

        // Convertirlos en el formato correcto para el frontend
        return schedules.stream().map(schedule -> {
            WorkingHoursNewScheduleDTO scheduledto = new WorkingHoursNewScheduleDTO();

            scheduledto.setExceptionDate(schedule.getExceptionDate());
            scheduledto.setStartTime(schedule.getStartTime());
            scheduledto.setEndTime(schedule.getEndTime());
            scheduledto.setReason(schedule.getReason() != null ? schedule.getReason() : "Motivo no especificado");
            return scheduledto;
        }).collect(Collectors.toList());
    }

    public List<DefaultWorkingHoursDTO> loadDefaultWorkingHours() {
        List <DefaultWorkingHours> defaultHours = defaultWorkingHoursDAO.findAll();

        return defaultHours.stream().map(defaultHour -> {
            DefaultWorkingHoursDTO defaultHourdto = new DefaultWorkingHoursDTO();

            defaultHourdto.setDayOfWeek(defaultHour.getDayOfWeek());
            defaultHourdto.setDayStartTime(defaultHour.getDayStartTime());
            defaultHourdto.setDayEndTime(defaultHour.getDayEndTime());
            return defaultHourdto;
        }).collect(Collectors.toList());
    }

}

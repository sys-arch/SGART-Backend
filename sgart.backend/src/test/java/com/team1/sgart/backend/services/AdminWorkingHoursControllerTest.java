package com.team1.sgart.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.team1.sgart.backend.http.AdminWorkingHoursController;
import com.team1.sgart.backend.model.CalendarEventDTO;
import com.team1.sgart.backend.model.DefaultWorkingHoursDTO;
import com.team1.sgart.backend.model.WorkingHoursNewScheduleDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdminWorkingHoursControllerTest {

    @Mock
    private CalendarEventService calendarEventService;

    @InjectMocks
    private AdminWorkingHoursController adminWorkingHoursController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadDefaultWorkingHours() {
        DefaultWorkingHoursDTO defaultHour1 = new DefaultWorkingHoursDTO();
        defaultHour1.setDayOfWeek(1);
        defaultHour1.setDayStartTime(LocalTime.of(8, 0));
        defaultHour1.setDayEndTime(LocalTime.of(16, 0));

        DefaultWorkingHoursDTO defaultHour2 = new DefaultWorkingHoursDTO();
        defaultHour2.setDayOfWeek(2);
        defaultHour2.setDayStartTime(LocalTime.of(8, 0));
        defaultHour2.setDayEndTime(LocalTime.of(16, 0));

        List<DefaultWorkingHoursDTO> mockResponse = Arrays.asList(defaultHour1, defaultHour2);

        when(calendarEventService.loadDefaultWorkingHours()).thenReturn(mockResponse);

        ResponseEntity<List<DefaultWorkingHoursDTO>> response = adminWorkingHoursController.loadDefaultWorkingHours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(calendarEventService, times(1)).loadDefaultWorkingHours();
    }

    @Test
    public void testLoadModifiedWorkingHours() {
        WorkingHoursNewScheduleDTO modifiedHour1 = new WorkingHoursNewScheduleDTO();
        modifiedHour1.setExceptionDate(LocalDate.of(2024, 11, 1));
        modifiedHour1.setStartTime(LocalTime.of(9, 0));
        modifiedHour1.setEndTime(LocalTime.of(17, 0));
        modifiedHour1.setReason("Motivo 1");

        WorkingHoursNewScheduleDTO modifiedHour2 = new WorkingHoursNewScheduleDTO();
        modifiedHour2.setExceptionDate(LocalDate.of(2024, 11, 2));
        modifiedHour2.setStartTime(LocalTime.of(10, 0));
        modifiedHour2.setEndTime(LocalTime.of(18, 0));
        modifiedHour2.setReason("Motivo 2");

        List<WorkingHoursNewScheduleDTO> mockResponse = Arrays.asList(modifiedHour1, modifiedHour2);

        when(calendarEventService.loadScheduleExceptions()).thenReturn(mockResponse);

        ResponseEntity<List<WorkingHoursNewScheduleDTO>> response = adminWorkingHoursController.loadScheduleExceptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(calendarEventService, times(1)).loadScheduleExceptions();
    }

    @Test
    public void testSaveEvent() {
        CalendarEventDTO newEvent = new CalendarEventDTO();
        newEvent.setEventTitle("Evento Test");
        newEvent.setEventStartDate(LocalDate.of(2024, 11, 5));
        newEvent.setEventTimeStart(LocalTime.of(14, 0));
        newEvent.setEventTimeEnd(LocalTime.of(15, 0));
        newEvent.setEventAllDay(false);
        newEvent.setEventFrequency("Una vez");
        newEvent.setEventRepetitionsCount(1);

        doNothing().when(calendarEventService).saveEvent(newEvent);

        ResponseEntity<Map<String,String>> response = adminWorkingHoursController.saveEvent(newEvent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(calendarEventService, times(1)).saveEvent(newEvent);
    }

    @Test
    public void testSaveDay() {
        WorkingHoursNewScheduleDTO newSchedule = new WorkingHoursNewScheduleDTO();
        newSchedule.setExceptionDate(LocalDate.of(2024, 11, 10));
        newSchedule.setStartTime(LocalTime.of(8, 0));
        newSchedule.setEndTime(LocalTime.of(15, 0));
        newSchedule.setReason("Motivo Prueba");

        doNothing().when(calendarEventService).saveWorkingHourSchedule(newSchedule);

        ResponseEntity<Map<String,String>> response = adminWorkingHoursController.saveWorkingDayException(newSchedule);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(calendarEventService, times(1)).saveWorkingHourSchedule(newSchedule);
    }

    @Test
    public void testLoadEvents() {
        CalendarEventDTO event1 = new CalendarEventDTO();
        event1.setEventTitle("Evento 1");
        event1.setEventStartDate(LocalDate.of(2024, 11, 5));
        event1.setEventTimeStart(LocalTime.of(10, 0));
        event1.setEventTimeEnd(LocalTime.of(11, 0));
        event1.setEventAllDay(false);
        event1.setEventFrequency("Semanal");
        event1.setEventRepetitionsCount(-1);

        CalendarEventDTO event2 = new CalendarEventDTO();
        event2.setEventTitle("Evento 2");
        event2.setEventStartDate(LocalDate.of(2024, 11, 6));
        event2.setEventTimeStart(LocalTime.of(12, 0));
        event2.setEventTimeEnd(LocalTime.of(13, 0));
        event2.setEventAllDay(true);
        event2.setEventFrequency("Diario");
        event2.setEventRepetitionsCount(-1);

        List<CalendarEventDTO> mockResponse = Arrays.asList(event1, event2);

        when(calendarEventService.loadAllEvents()).thenReturn(mockResponse);

        ResponseEntity<List<CalendarEventDTO>> response = adminWorkingHoursController.loadEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(calendarEventService, times(1)).loadAllEvents();
    }

}

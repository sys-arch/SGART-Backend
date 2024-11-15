package com.team1.sgart.backend.http;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.team1.sgart.backend.dao.AbsencesDao;
import com.team1.sgart.backend.model.Absences;
import com.team1.sgart.backend.model.AbsencesDTO;
import com.team1.sgart.backend.services.AbsencesService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AbsencesControllerTest {

    @Mock
    private AbsencesDao absencesDao;

    @InjectMocks
    private AbsencesService absencesService;

    private AbsencesController absencesController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        absencesController = new AbsencesController(absencesService);
    }

    @Test
    void testGetAbsencesByUser() {
        UUID userId = UUID.randomUUID();
        List<Absences> mockAbsences = new ArrayList<>();

        Absences absence = new Absences();
        absence.setAbsenceId(UUID.randomUUID());
        absence.setUserId(userId);
        absence.setAbsenceStartDate(LocalDate.now());
        absence.setAbsenceEndDate(LocalDate.now().plusDays(1));
        absence.setAbsenceAllDay(true);
        absence.setAbsenceReason("Test Reason");
        mockAbsences.add(absence);

        when(absencesDao.findByUserId(userId)).thenReturn(mockAbsences);

        List<AbsencesDTO> absencesDTOList = absencesService.getAbsencesByUser(userId);
        assertEquals(1, absencesDTOList.size());
        assertEquals("Test Reason", absencesDTOList.get(0).getAbsenceReason());

        ResponseEntity<List<AbsencesDTO>> response = absencesController.getAbsencesByUser(userId);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetAbsencesByUser_NotFound() {
        UUID userId = UUID.randomUUID();
        when(absencesDao.findByUserId(userId)).thenReturn(new ArrayList<>());

        ResponseEntity<List<AbsencesDTO>> response = absencesController.getAbsencesByUser(userId);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreateAbsence() {
        UUID userId = UUID.randomUUID();

        AbsencesDTO absenceDto = new AbsencesDTO();
        absenceDto.setUserId(userId);
        absenceDto.setAbsenceStartDate(LocalDate.now());
        absenceDto.setAbsenceEndDate(LocalDate.now().plusDays(1));
        absenceDto.setAbsenceReason("Test Reason");
        absenceDto.setAbsenceAllDay(true);

        Absences mockAbsence = new Absences();
        mockAbsence.setAbsenceId(UUID.randomUUID());
        mockAbsence.setUserId(userId);
        mockAbsence.setAbsenceStartDate(LocalDate.now());
        mockAbsence.setAbsenceEndDate(LocalDate.now().plusDays(1));
        mockAbsence.setAbsenceReason("Test Reason");
        mockAbsence.setAbsenceAllDay(true);

        when(absencesDao.save(any(Absences.class))).thenReturn(mockAbsence);

        AbsencesDTO createdAbsence = absencesService.createAbsence(absenceDto);
        assertNotNull(createdAbsence);
        assertEquals("Test Reason", createdAbsence.getAbsenceReason());

        ResponseEntity<AbsencesDTO> response = absencesController.createAbsence(absenceDto);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Reason", response.getBody().getAbsenceReason());
    }

    @Test
    void testCreateAbsence_BadRequest() {
        ResponseEntity<AbsencesDTO> response = absencesController.createAbsence(null);
        assertEquals(400, response.getStatusCodeValue());
    }
}

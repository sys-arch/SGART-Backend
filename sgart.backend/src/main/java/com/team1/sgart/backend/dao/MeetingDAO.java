package com.team1.sgart.backend.dao;

import com.team1.sgart.backend.model.Meeting;
import com.team1.sgart.backend.model.User;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Repository
public interface MeetingDAO extends JpaRepository<Meeting, UUID> {

    // Método para obtener una reunión por su ID
    Optional<Meeting> findById(UUID meetingId);
    
    // Método para editar una reunión
    
    default Meeting updateMeeting(Meeting meeting, Meeting updatedMeeting) {
        
        // Actualizar los campos
        actualizarCampo(meeting::setTitle, updatedMeeting.getTitle());
        actualizarCampo(meeting::setAllDay, updatedMeeting.isAllDay());
        actualizarCampo(meeting::setStartTime, updatedMeeting.getStartTime());
        actualizarCampo(meeting::setEndTime, updatedMeeting.getEndTime());
        actualizarCampo(meeting::setOrganizer, updatedMeeting.getOrganizer());
        actualizarCampo(meeting::setLocation, updatedMeeting.getLocation());
        actualizarCampo(meeting::setObservations, updatedMeeting.getObservations());

        // Guardar la reunión actualizada
        return save(meeting);
    }

    private <T> void actualizarCampo(Consumer<T> setter, T nuevoValor) {
        if (nuevoValor != null && !(nuevoValor instanceof String str && str.isEmpty())) {
            setter.accept(nuevoValor);
        }
    }
    
    @Query("SELECT m FROM Meeting m WHERE " +
    	       "m.startTime = :startTime AND " +
    	       "m.endTime = :endTime AND " +
    	       "(m.organizer = :organizer OR :organizer MEMBER OF m.attendees)")
    List<Meeting> findConflictingMeetings(@Param("startTime") Time startTime,
    	                                      @Param("endTime") Time endTime,
    	                                      @Param("organizer") User organizer);

    
}

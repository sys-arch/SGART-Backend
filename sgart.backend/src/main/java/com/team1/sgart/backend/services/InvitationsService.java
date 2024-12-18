package com.team1.sgart.backend.services;

import java.util.List;
import java.util.UUID;
import com.team1.sgart.backend.dao.InvitationsDao;
import com.team1.sgart.backend.dao.MeetingsDao;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.Meetings;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InvitationsService {
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Autowired
	private MeetingsDao meetingsDao;

	@Autowired
	private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(InvitationsService.class);
    private final InvitationsDao invitationsDao;


    @Autowired
    public InvitationsService(InvitationsDao invitationsDao) {
        this.invitationsDao = invitationsDao;
        logger.info("[!] InvitationsService created");
    }

    public boolean updateInvitationStatusWithNotification(UUID meetingId, UUID userId, String newStatus, String comment) {
        // Actualiza el estado usando la lógica original
        boolean updated = updateInvitationStatusLogic(meetingId, userId, newStatus, comment);

        // Si la actualización fue exitosa, notifica al usuario
        if (updated) {
            notificarActualizacionEstado(meetingId, userId, newStatus);
        }

        return updated;
    }

    // Método privado que contiene la lógica original
    private boolean updateInvitationStatusLogic(UUID meetingId, UUID userId, String newStatus, String comment) {
        try {
            logger.debug("Updating invitation - Meeting: {}, User: {}, Status: {}", meetingId, userId, newStatus);

            int count = invitationsDao.countByMeetingIdAndUserId(meetingId, userId);
            if (count == 0) {
                logger.warn("Invitation not found for Meeting: {} and User: {}", meetingId, userId);
                return false;
            }

            return invitationsDao.updateInvitationStatus(meetingId, userId, newStatus, comment) > 0;

        } catch (Exception e) {
            logger.error("Error updating invitation status", e);
            throw new RuntimeException("Error updating invitation status", e);
        }
    }
    public boolean updateUserAttendance(UUID meetingId, UUID userId) {
        try {
            logger.debug("Updating user attendance - Meeting: {}, User: {}", 
                      meetingId, userId);
            
            int rowsUpdated = invitationsDao.updateUserAttendance(meetingId, userId);
            return rowsUpdated > 0;
            
        } catch (Exception e) {
            logger.error("Error updating user attendance", e);
            throw new RuntimeException("Error updating user attendance", e);
        }
    }

    public Integer getUserAttendance(UUID meetingId, UUID userId) {
        if (meetingId == null || userId == null) {
            logger.error("Meeting ID or User ID is null");
            throw new IllegalArgumentException("Meeting ID and User ID cannot be null");
        }

        try {
            logger.debug("Getting user attendance - Meeting: {}, User: {}", 
                      meetingId, userId);
            Integer attendance = invitationsDao.getUserAttendance(meetingId, userId);
            
            if (attendance == null) {
                logger.debug("No attendance record found for Meeting: {} and User: {}", 
                          meetingId, userId);
            }
            
            return attendance;
            
        } catch (Exception e) {
            logger.error("Error getting user attendance for Meeting: {} and User: {}: {}", 
                      meetingId, userId, e.getMessage(), e);
            throw new RuntimeException("Error retrieving user attendance: " + e.getMessage(), e);
        }
    }

    public boolean inviteUsers(UUID meetingId, List<UUID> userIds) {
    	logger.debug("Datos recibidos en inviteUsers: meetingId={}, userIds={}", meetingId, userIds);

        if (meetingId == null || userIds == null || userIds.isEmpty()) {
            logger.error("Meeting ID is null or users list is empty");
            return false;
        }

        try {
            logger.debug("Creating invitations for meeting: {} and {} users", 
                      meetingId, userIds.size());
            
            // Por cada usuario en la lista, crear una invitación
            int successCount = 0;
            for (UUID userId : userIds) {
                // Verificar si ya existe una invitación para este usuario en esta reunión
                if (invitationsDao.countByMeetingIdAndUserId(meetingId, userId) > 0) {
                    logger.warn("Invitation already exists for user {} in meeting {}", 
                             userId, meetingId);
                    continue;
                }
                
                // Crear nueva invitación con estado "Pendiente"
                int result = invitationsDao.createInvitation(
                    meetingId,
                    userId,
                    "Pendiente",
                    null
                );
                if (result > 0) {
                    successCount++;
                }
                
             // Llamada al método independiente para generar notificación
                notificarInvitacion(meetingId, userId);
            }
            
            logger.info("Successfully created {} invitations out of {} users", 
                     successCount, userIds.size());
            
            return successCount > 0;
            
        } catch (Exception e) {
            logger.error("Error creating invitations for meeting {}: {}", 
                      meetingId, e.getMessage(), e);
            throw new RuntimeException("Error creating invitations", e);
        }
    }
    
 // Notificar al usuario invitado
    public void notificarInvitacion(UUID meetingId, UUID userId) {
        try {
            // Obtener detalles de la reunión
            Meetings meeting = meetingsDao.findById(meetingId)
                    .orElseThrow(() -> new RuntimeException("Reunión no encontrada"));

            // Obtener el nombre completo del organizador
            String organizerName = userDao.findUserFullNameById(meeting.getOrganizerId());

            // Crear el mensaje de la notificación
            String mensaje = String.format(
                "Has sido invitado a la reunión \"%s\" organizada por %s el día %s.",
                meeting.getMeetingTitle(),
                organizerName,
                meeting.getMeetingDate()
            );

            // Crear la notificación
            notificacionService.crearNotificacion(
                    userId,
                    "Nueva invitación a reunión",
                    mensaje
            );

        } catch (Exception e) {
            throw new RuntimeException("Error al generar la notificación: " + e.getMessage(), e);
        }
    }
    
 // Notificar actualización del estado de invitación
    public void notificarActualizacionEstado(UUID meetingId, UUID userId, String nuevoEstado) {
        try {
            // Obtener detalles de la reunión
            Meetings meeting = meetingsDao.findById(meetingId)
                    .orElseThrow(() -> new RuntimeException("Reunión no encontrada"));

            // Obtener el nombre completo del invitado
            String invitadoNombre = userDao.findUserFullNameById(userId);

            // Obtener el ID del organizador
            UUID organizerId = meeting.getOrganizerId();

            // Crear el mensaje para el organizador
            String mensajeOrganizador = String.format(
                "El usuario %s ha %s la invitación a la reunión \"%s\" programada para el día %s.",
                invitadoNombre,
                nuevoEstado.equalsIgnoreCase("ACEPTADA") ? "aceptado" : "rechazado",
                meeting.getMeetingTitle(),
                meeting.getMeetingDate()
            );

            // Crear la notificación para el organizador
            notificacionService.crearNotificacion(
                    organizerId, // ID del organizador
                    "Actualización de invitación",
                    mensajeOrganizador
            );

        } catch (Exception e) {
            throw new RuntimeException("Error al generar la notificación de estado para el organizador: " + e.getMessage(), e);
        }
    }

}

package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.NotificacionDao;
import com.team1.sgart.backend.model.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionDao notificaciondao;

    public void crearNotificacion(UUID usuarioId, String titulo, String mensaje) {
        Notificacion notificacion = new Notificacion(usuarioId, titulo, mensaje);
        notificaciondao.save(notificacion);
    }

    public List<Notificacion> obtenerNotificaciones(UUID usuarioId) {
        return notificaciondao.findByUsuarioId(usuarioId);
    }

    public void eliminarNotificacion(UUID id) {
        notificaciondao.deleteById(id);
    }

    @Transactional
    public void eliminarTodas(UUID usuarioId) {
        notificaciondao.deleteByUsuarioId(usuarioId);
    }
    
    public void marcarComoLeidas(UUID userId) {
        List<Notificacion> notificaciones = notificaciondao.findByUsuarioId(userId);
        notificaciones.forEach(notificacion -> notificacion.setLeida(true));
        notificaciondao.saveAll(notificaciones);
    }
}
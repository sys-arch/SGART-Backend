package com.team1.sgart.backend.dao;

import com.team1.sgart.backend.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificacionDao extends JpaRepository<Notificacion, UUID> {
    List<Notificacion> findByUsuarioId(UUID usuarioId);
    void deleteByUsuarioId(UUID usuarioId);
}

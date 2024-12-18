package com.team1.sgart.backend.http;

import com.team1.sgart.backend.model.Notificacion;
import com.team1.sgart.backend.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // Obtener notificaciones del usuario autenticado
    @GetMapping
    public List<Notificacion> obtenerNotificaciones(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName()); // Obtener userId del token
        return notificacionService.obtenerNotificaciones(userId);
    }

    // Eliminar una notificación individual
    @DeleteMapping("/{id}")
    public void eliminarNotificacion(@PathVariable UUID id) {
        notificacionService.eliminarNotificacion(id);
    }

    // Eliminar todas las notificaciones del usuario autenticado
    @DeleteMapping
    public void eliminarTodas(Authentication authentication, @RequestParam(required = false) UUID userId) {
        UUID usuarioId;

        // Si se proporciona un userId, usarlo; si no, usar el del token (usuario autenticado)
        if (userId != null) {
            usuarioId = userId;
        } else {
            usuarioId = UUID.fromString(authentication.getName());
        }

        notificacionService.eliminarTodas(usuarioId);
    }

    
    @PutMapping("/marcar-leidas")
    public void marcarNotificacionesComoLeidas(Authentication authentication) {
        // Extraer el userId desde el token JWT
        UUID userId = UUID.fromString(authentication.getName());
        notificacionService.marcarComoLeidas(userId);
    }
}

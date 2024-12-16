package com.team1.sgart.backend.http;

import com.team1.sgart.backend.model.Notificacion;
import com.team1.sgart.backend.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public List<Notificacion> obtenerNotificaciones(@RequestParam UUID usuarioId) {
        return notificacionService.obtenerNotificaciones(usuarioId);
    }

    @DeleteMapping("/{id}")
    public void eliminarNotificacion(@PathVariable UUID id) {
        notificacionService.eliminarNotificacion(id);
    }

    @DeleteMapping
    public void eliminarTodas(@RequestParam UUID usuarioId) {
        notificacionService.eliminarTodas(usuarioId);
    }
}
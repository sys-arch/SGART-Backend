package com.team1.sgart.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SGART_NotificationsTable")
public class Notificacion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private UUID usuarioId;
	private String titulo;
	private String mensaje;
	private LocalDateTime fechaCreacion;
	private boolean leida;

	// Constructor vacío
	public Notificacion() {
	}

	public Notificacion(UUID usuarioId, String titulo, String mensaje) {
		this.usuarioId = usuarioId;
		this.titulo = titulo;
		this.mensaje = mensaje;
		this.fechaCreacion = LocalDateTime.now();
		this.leida = false;
	}

	// Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(UUID usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public boolean isLeida() {
		return leida;
	}

	public void setLeida(boolean leida) {
		this.leida = leida;
	}
}
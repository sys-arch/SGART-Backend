package com.team1.sgart.backend.dao;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

import com.team1.sgart.backend.model.Admin;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

public interface AdminDao extends  JpaRepository<Admin, UUID> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByEmailAndPassword(String email, String password);
    
    @Query("SELECT a.twoFactorAuthCode FROM Admin a WHERE a.email = :email")
    String obtenerAuthCodePorEmail(@Param("email") String email);
	
    // Método para invertir el valor de "blocked" de un usuario
    @Modifying
    @Transactional
    @Query("UPDATE Admin a SET a.blocked=CASE a.blocked WHEN TRUE THEN FALSE ELSE TRUE END WHERE a.email = :email")
    void cambiarHabilitacionAdmin(@Param("email") String email);
	
    default Admin updateAdmin(String email, Admin updatedAdmin) {
	        Admin admin = findByEmail(email)
	                        .orElseThrow(() -> new EntityNotFoundException("Admin con email " + email + " no encontrado"));

	        // Actualizar los campos
	        actualizarCampo(admin::setName, updatedAdmin.getName());
	        actualizarCampo(admin::setLastName, updatedAdmin.getLastName());
	        actualizarCampo(admin::setCenter, updatedAdmin.getCenter());

	        // Guardar el admin actualizado
	        return save(admin);
	    }

    private <T> void actualizarCampo(Consumer<T> setter, T nuevoValor) {
        if (nuevoValor != null && !(nuevoValor instanceof String str && str.isEmpty())) {
            setter.accept(nuevoValor);
        }
    }

	@Modifying
    @Transactional
    void deleteByEmail(String email);  // Para eliminar por email
    
	@Modifying
    @Transactional
    @Query("INSERT INTO Admin (name, lastName, email, password, center, twoFactorAuthCode) VALUES (:#{#a.name}, :#{#a.lastName}, :#{#a.email}, :#{#a.password}, :#{#a.center}, :#{#a.twoFactorAuthCode})")
    void crearAdmin(@Param("a") Admin a);
}

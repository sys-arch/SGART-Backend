package com.team1.sgart.backend.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.team1.sgart.backend.model.User;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
  
    // Método para verificar si el usuario está validado
    @Query("SELECT u.validated FROM User u WHERE u.email = :email")
    Boolean isUsuarioValidado(@Param("email") String email);

    // Método para marcar al usuario como validado
    @Modifying
    @Query("UPDATE User u SET u.validated = true WHERE u.email = :email")
    void validarUsuario(@Param("email") String email);

    // Método para invertir el valor de "blocked" de un usuario
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.blocked=NOT u.blocked WHERE u.email = :email")
    void cambiarHabilitacionUsuario(@Param("email") String email);

    // Método para obtener la lista de usuarios que ya han sido validados.
    @Query("SELECT u FROM User u where validated=true")
	      Optional<List<User>> getUsuariosValidados();

    default User updateUser(String email, User updatedUser) {
        User user = findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado"));

        // Actualizar los campos
        actualizarCampo(user::setName, updatedUser.getName());
        actualizarCampo(user::setLastName, updatedUser.getLastName());
        actualizarCampo(user::setDepartment, updatedUser.getDepartment());
        actualizarCampo(user::setCenter, updatedUser.getCenter());
        actualizarCampo(user::setEmail, updatedUser.getEmail());
        actualizarCampo(user::setHiringDate, updatedUser.getHiringDate());
        actualizarCampo(user::setProfile, updatedUser.getProfile());

        // Guardar el usuario actualizado
        return save(user);
    }

    private <T> void actualizarCampo(Consumer<T> setter, T nuevoValor) {
        if (nuevoValor != null && !(nuevoValor instanceof String str && str.isEmpty())) {
            setter.accept(nuevoValor);
        }
    }
}


package com.team1.sgart.backend.dao;

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
        if (nuevoValor != null && !(nuevoValor instanceof String && ((String) nuevoValor).isEmpty())) {
            setter.accept(nuevoValor);
        }
    }

}

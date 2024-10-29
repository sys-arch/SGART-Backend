package com.team1.sgart.backend.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
<<<<<<< Updated upstream
import java.util.function.Consumer;
=======
>>>>>>> Stashed changes

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.team1.sgart.backend.model.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
  
    // Método para verificar si el usuario está validado
    @Query("SELECT u.validated FROM User u WHERE u.email = :email")
    Boolean isUsuarioValidado(@Param("email") String email);

    // Método para marcar al usuario como validado
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.validated = true WHERE u.email = :email")
    void validarUsuario(@Param("email") String email);

    // Método para invertir el valor de "blocked" de un usuario
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.blocked=CASE u.blocked WHEN TRUE THEN FALSE ELSE TRUE END WHERE u.email = :email")
    void cambiarHabilitacionUsuario(@Param("email") String email);
<<<<<<< Updated upstream
    
 // Método para obtener la lista de usuarios que quedan por validar.
    @Query("SELECT u FROM User u where u.validated=false")
	      Optional<List<User>> getUsuariosSinValidar();

    // Método para obtener la lista de usuarios que ya han sido validados.
    @Query("SELECT u FROM User u where u.validated=true")
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
    
    @Transactional
    void deleteByEmail(String email);  // Para eliminar por email
    @Transactional
    void deleteById(Integer id);  // O id si se da el caso
      
=======

    // Método para obtener el authCode de un usuario
    @Query("SELECT u.user_twoFactorAuthCode FROM SGART_UsersTable u WHERE u.user_email = :email")
    static
    String obtenerAuthCodePorEmail(@Param("email") String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerAuthCodePorEmail'");
    }
    
>>>>>>> Stashed changes
}


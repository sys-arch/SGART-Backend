package com.team1.sgart.backend.dao;

import com.team1.sgart.backend.model.Invitations;
import com.team1.sgart.backend.model.User;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findById(UUID id);

    // Método para obtener el usuario actual
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findCurrentUser(@Param("email") String email);

    // Método para obtener todos los usuarios habilitados
    @Query("SELECT u FROM User u WHERE u.blocked = false")
    List<User> findAllNotBlocked();

    // Método para comprobar la disponibilidad de un usuario en una reunión
    @Query("SELECT i FROM Invitations i WHERE i.user = :user AND i.meeting.meetingStartTime <= :endTime AND i.meeting.meetingEndTime >= :startTime")
    List<Invitations> checkUserAvailability(@Param("user") User user, @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

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
    @Query("UPDATE User u SET u.blocked = CASE u.blocked WHEN TRUE THEN FALSE ELSE TRUE END WHERE u.email = :email")
    void cambiarHabilitacionUsuario(@Param("email") String email);

    // Método para obtener la lista de usuarios que quedan por validar
    @Transactional
    @Query("SELECT u FROM User u WHERE u.validated = false")
    Optional<List<User>> getUsuariosSinValidar();

    // Método para obtener la lista de usuarios que ya han sido validados
    @Query("SELECT u FROM User u WHERE u.validated = true")
    Optional<List<User>> getUsuariosValidados();
    
    @Modifying
    @Transactional
    void deleteByEmail(String email);

    // Método para obtener el authCode de un usuario
    @Query("SELECT u.twoFactorAuthCode FROM User u WHERE u.email = :email")
    String obtenerAuthCodePorEmail(@Param("email") String email);

    @Query("SELECT CONCAT(u.name, ' ', u.lastName) FROM User u WHERE u.id = :id")
    String findUserFullNameById(@Param("id") UUID id);
    
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    @Transactional
    void updatePassword(@Param("userId") UUID userId, @Param("newPassword") String newPassword);

}

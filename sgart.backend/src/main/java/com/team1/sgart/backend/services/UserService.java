package com.team1.sgart.backend.services;

import com.team1.sgart.backend.model.*;
import com.team1.sgart.backend.util.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.dao.AdminDao;
import com.team1.sgart.backend.dao.UserDao;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import org.mindrot.jbcrypt.BCrypt;

@Service
@Transactional
public class UserService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_TIME = 15 * 60 * 1000; // 15 minutos de bloqueo

    private final UserDao userDao;
    private final AdminDao adminDao;
    private final JwtTokenProvider jwtTokenProvider;

    private ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> blockedSessions = new ConcurrentHashMap<>();

    @Autowired
    public UserService(UserDao userDao, AdminDao adminDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.adminDao = adminDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User registrarUser(User user) {
        // Comprobar si el email ya está registrado
        if (emailYaRegistrado(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        return userDao.save(user);
    }

    public void modificarUser(User user) {
        String email = user.getEmail();
        if (!emailYaRegistrado(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El email no está registrado");
        } else if (!emailFormatoValido(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto");
        } else {
            Optional<User> userToModify = userDao.findByEmail(email);
            if (userToModify.isPresent()) {
                User updatedUser = userToModify.get();
                actualizarCampo(updatedUser::setName, user.getName());
                actualizarCampo(updatedUser::setLastName, user.getLastName());
                actualizarCampo(updatedUser::setDepartment, user.getDepartment());
                actualizarCampo(updatedUser::setCenter, user.getCenter());
                actualizarCampo(updatedUser::setEmail, user.getEmail());
                actualizarCampo(updatedUser::setHiringDate, user.getHiringDate());
                actualizarCampo(updatedUser::setProfile, user.getProfile());
                userDao.save(updatedUser);
            }
        }
    }

    public void modificarPerfilUser(UserDTO changesInUser) {
        UUID userId = changesInUser.getID();
        Optional<User> userToModify = userDao.findById(userId);
        if (userToModify.isPresent()) {
            User updatedUser = userToModify.get();
            actualizarCampo(updatedUser::setName, changesInUser.getName());
            actualizarCampo(updatedUser::setLastName, changesInUser.getLastName());
            actualizarCampo(updatedUser::setDepartment, changesInUser.getDepartment());
            actualizarCampo(updatedUser::setCenter, changesInUser.getCenter());
            actualizarCampo(updatedUser::setProfile, changesInUser.getProfile());
            userDao.save(updatedUser);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    public GenericUser loginUser(User user, HttpSession session) {
        String sessionId = session.getId();
        if (isSessionBlocked(sessionId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Inicio de sesión bloqueado temporalmente. Inténtelo más tarde.");
        }

        String email = user.getEmail();
        String password = user.getPassword();

        if (!emailFormatoValido(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto");
        } else if (userDao.findByEmailAndPassword(email, password).isPresent()) {
            resetAttempts(sessionId);
            user = userDao.findByEmail(user.getEmail()).get();
            if (user.isBlocked()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cuenta bloqueada. Contacte al soporte.");
            }
            return user;
        } else if (adminDao.findByEmailAndPassword(email, password).isPresent()) {
            resetAttempts(sessionId);
            Admin admin = adminDao.findByEmail(user.getEmail()).get();
            return admin;
        } else {
            incrementAttempts(sessionId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }
    }

    private boolean isSessionBlocked(String sessionId) {
        if (blockedSessions.containsKey(sessionId)) {
            long blockTime = blockedSessions.get(sessionId);
            if (System.currentTimeMillis() - blockTime > BLOCK_TIME) {
                blockedSessions.remove(sessionId);
                return false;
            }
            return true;
        }
        return false;
    }

    private void incrementAttempts(String sessionId) {
        loginAttempts.merge(sessionId, 1, Integer::sum);
        if (loginAttempts.get(sessionId) > MAX_ATTEMPTS) {
            blockedSessions.put(sessionId, System.currentTimeMillis());
            loginAttempts.remove(sessionId);
        }
    }

    private void resetAttempts(String sessionId) {
        loginAttempts.remove(sessionId);
    }

    public boolean emailFormatoValido(User user) {
        boolean emailValido = false;

        if (user.comprobarFormatoEmail())
            emailValido = true;

        return emailValido;
    }

    public boolean emailYaRegistrado(User user) {
        boolean yaRegistrado = true;

        if (!userDao.findByEmail(user.getEmail()).isPresent())
            yaRegistrado = false;

        return yaRegistrado;
    }

    public boolean passwordFormatoValido(User user) {
        boolean passwordValido = false;

        if (user.comprobarFormatoPassword())
            passwordValido = true;

        return passwordValido;
    }

    public List<UserAbsenceDTO> loadUsers() {
        List<User> users = userDao.findAll();
        return users.stream().map(user -> {
            UserAbsenceDTO userAbsenceDTO = new UserAbsenceDTO();
            userAbsenceDTO.setId(user.getID());
            userAbsenceDTO.setEmail(user.getEmail());
            userAbsenceDTO.setFirstName(user.getName());
            userAbsenceDTO.setLastName(user.getLastName());
            userAbsenceDTO.setCenter(user.getCenter());
            userAbsenceDTO.setProfile(user.getProfile());
            return userAbsenceDTO;
        }).collect(Collectors.toList());
    }

    private <T> void actualizarCampo(Consumer<T> setter, T nuevoValor) {
        if (nuevoValor != null && !(nuevoValor instanceof String str && str.isEmpty())) {
            setter.accept(nuevoValor);
        }
    }

    public Optional<User> getUserById(UUID userId) {
        return userDao.findById(userId);
    }

    public void resetPassword(String token, String newPassword) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido o expirado");
        }

        try {
            String email = jwtTokenProvider.getEmailFromToken(token);
            User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

            // Validar el formato de la nueva contraseña
            user.setPassword(newPassword);
            if (!passwordFormatoValido(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de contraseña inválido");
            }

            // Actualizar la contraseña directamente sin encriptar
            userDao.updatePassword(user.getID(), newPassword);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Error al restablecer la contraseña: " + e.getMessage());
        }
    }

    public String generatePasswordResetToken(String email) {
        User user = userDao.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe un usuario con ese email"));

        if (user.isBlocked()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Esta cuenta está bloqueada");
        }

        return jwtTokenProvider.generatePasswordResetToken(user);
    }
}
package com.team1.sgart.backend.services;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

@Service
@Transactional
public class UserService {
	
	
	    private static final int MAX_ATTEMPTS = 3;
	    private static final long BLOCK_TIME = (15 * 60 * 1000L); // 15 minutos de bloqueo

	    private final UserDao userDao;
	    private final AdminDao adminDao;
	    private final JwtTokenProvider jwtTokenProvider;

	    @Autowired
	    private ValidationService validationService;
	    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
	        if (validationService.emailExisteEnSistema(user.getEmail())) {
	            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
	        }
	        // Validar y hashear la contraseña
	        user.setPassword(validarYHashearPassword(user.getPassword()));

	        return userDao.save(user);
	    }

	    public void modificarUser(User user) {
	        Optional<User> userToModify = userDao.findByEmail(user.getEmail());
	        if (userToModify.isEmpty()) {
	        	System.out.println("Email no encontrado: " + user.getEmail()); // Agrega esto
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El email no está registrado");	        }

	        User updatedUser = userToModify.get();
	        actualizarCampo(updatedUser::setName, user.getName());
	        actualizarCampo(updatedUser::setLastName, user.getLastName());
	        actualizarCampo(updatedUser::setDepartment, user.getDepartment());
	        actualizarCampo(updatedUser::setCenter, user.getCenter());
	        actualizarCampo(updatedUser::setHiringDate, user.getHiringDate());
	        actualizarCampo(updatedUser::setProfile, user.getProfile());

	        // Validar y hashear nueva contraseña si es necesario
	        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
	            updatedUser.setPassword(validarYHashearPassword(user.getPassword()));
	        }

	        userDao.save(updatedUser);
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
	        }

	        // Verificar si el usuario es un Admin
	        Optional<Admin> optionalAdmin = adminDao.findByEmail(email);
	        if (optionalAdmin.isPresent()) {
	            Admin admin = optionalAdmin.get();
	            if (passwordEncoder.matches(password, admin.getPassword())) {
	                resetAttempts(sessionId);
	                if (admin.getBlocked()) {
	                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cuenta de administrador bloqueada. Contacte al soporte.");
	                }
	                return admin; // Devuelve el Admin autenticado
	            } else {
	                incrementAttempts(sessionId);
	                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta.");
	            }
	        }

	        // Verificar si el usuario es un User
	        Optional<User> optionalUser = userDao.findByEmail(email);
	        if (optionalUser.isPresent()) {
	            User existingUser = optionalUser.get();
	            if (passwordEncoder.matches(password, existingUser.getPassword())) {
	                resetAttempts(sessionId);
	                if (existingUser.isBlocked()) {
	                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cuenta bloqueada. Contacte al soporte.");
	                }
	                return existingUser; // Devuelve el User autenticado
	            } else {
	                incrementAttempts(sessionId);
	                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta.");
	            }
	        }

	        // Si no se encuentra ni como User ni como Admin
	        incrementAttempts(sessionId);
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email no encontrado.");
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
	        return user.comprobarFormatoEmail();
	    }

	    private String validarYHashearPassword(String password) {
	        if (password == null || password.isEmpty()) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña no puede estar vacía");
	        }
	        if (!comprobarFormatoPassword(password)) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de contraseña inválido");
	        }
	        return passwordEncoder.encode(password);
	    }

	    private boolean comprobarFormatoPassword(String password) {
	        int longitudMinima = 8;
	        String mayus = ".*[A-Z].*";
	        String minus = ".*[a-z].*";
	        String digit = ".*\\d.*";
	        String specialCharacters = ".*[!@#\\$%\\^&\\*].*";

	        return password.length() >= longitudMinima &&
	               password.matches(mayus) &&
	               password.matches(minus) &&
	               password.matches(digit) &&
	               password.matches(specialCharacters);
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

	        String email = jwtTokenProvider.getEmailFromToken(token);
	        User user = userDao.findByEmail(email)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

	        // Validar y hashear la nueva contraseña
	        userDao.updatePassword(user.getID(), validarYHashearPassword(newPassword));
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

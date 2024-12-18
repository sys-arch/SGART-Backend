package com.team1.sgart.backend.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.services.UserService;
import com.team1.sgart.backend.util.JwtTokenProvider;


import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserService userService;
	private static final String USER_ID = "userId";
	private static final String SUCCESS_KEY = "success";
	private static final String MESSAGE_KEY = "message";
	@Autowired
	private UserDao userDao;


	@Autowired
	UserController(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;


	@PostMapping("/registro")
	public ResponseEntity<String> registrar(@RequestBody User user) {

		userService.registrarUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");
	}

	@PostMapping("/modificar")
	public ResponseEntity<Map<String, String>> modificar(@RequestBody User user) {

		userService.modificarUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Perfil modificado correctamente"));
	}

	@PostMapping("/modificarPerfil")
    public ResponseEntity<String> modificarPerfil(@RequestBody UserDTO changesInUser) {
        try {
            userService.modificarPerfilUser(changesInUser);
            return ResponseEntity.status(HttpStatus.OK).body("Perfil modificado correctamente");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpSession session) {
	    try {
	        // Autenticar al usuario
	        GenericUser authenticatedUser = userService.loginUser(user, session);
	        if (authenticatedUser == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of(SUCCESS_KEY , false,MESSAGE_KEY, "Credenciales incorrectas"));
	        }

	        // Generar el token JWT
	        String token = jwtTokenProvider.generateToken(authenticatedUser);

	        // Crear la respuesta
	        Map<String, Object> response = new HashMap<>();
	        response.put(SUCCESS_KEY , true);
	        response.put("token", token);
	        logger.info("Token generado: " + token);

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of(SUCCESS_KEY , false, MESSAGE_KEY, e.getMessage()));
	    }
	}




	@PostMapping("/verificar-email")
	public ResponseEntity<String> verificarEmail(@RequestBody User user) {
	    try {
	        logger.info("Verificando email: " + user.getEmail());
	        Optional<User> existingUser = userDao.findByEmail(user.getEmail());
	        if (existingUser.isPresent()) {
	            logger.info("El email ya está registrado");
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email está registrado");
	        } else {
	            logger.info("El email no está registrado");
	            return ResponseEntity.status(HttpStatus.OK).body("El email no está registrado");
	        }
	    } catch (Exception e) {
	        logger.error("Error al verificar el email", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el email");
	    }
	}


    @GetMapping("/cargarUsuarios")
    public ResponseEntity<List<UserAbsenceDTO>> getAllUsers() {
        List<UserAbsenceDTO> usersList = userService.loadUsers();
        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/current/userId")
    public ResponseEntity<Map<String, String>> getCurrentUserId(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7); // Remueve "Bearer "
        String userId = jwtTokenProvider.getUserIdFromToken(token); // Extrae el userId del token
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> response = new HashMap<>();
        response.put(USER_ID, userId);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/current/user")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7); // Remueve "Bearer "
        String userId = jwtTokenProvider.getUserIdFromToken(token); // Extrae el userId del token

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> currentUser = userService.getUserById(UUID.fromString(userId));
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        User user = currentUser.get();
        user.setPassword(""); // Eliminar la contraseña por seguridad
        return ResponseEntity.ok(user);
    }

}

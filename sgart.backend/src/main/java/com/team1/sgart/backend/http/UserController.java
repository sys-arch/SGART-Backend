package com.team1.sgart.backend.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
	public ResponseEntity<String> modificar(@RequestBody User user) {

		userService.modificarUser(user);
		return ResponseEntity.status(HttpStatus.OK).body("Perfil modificado correctamente");
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
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
	    try {
	        // Autenticar al usuario
	        GenericUser genericUser = userService.loginUser(user, null);

	        if (genericUser == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false));
	        }

	        // Crear el token
	        String token = jwtTokenProvider.generateToken(genericUser);

	        // Respuesta con booleano y token
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("token", token);

	        return ResponseEntity.ok(response);

	    } catch (ResponseStatusException e) {
	        return ResponseEntity.status(e.getStatusCode()).body(Map.of("success", false, "error", e.getReason()));
	    }
	}



    @PostMapping("/verificar-email")
    public ResponseEntity<String> verificarEmail(@RequestBody User user) {
        boolean existe = userService.emailYaRegistrado(user);
        if (!existe) {
            return ResponseEntity.status(HttpStatus.OK).body("El email no está registrado");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email está registrado");
        }
    }

    @GetMapping("/cargarUsuarios")
    public ResponseEntity<List<UserAbsenceDTO>> getAllUsers() {
        List<UserAbsenceDTO> usersList = userService.loadUsers();
        return ResponseEntity.ok(usersList);
    }

	@GetMapping("/current/userId")
	public ResponseEntity<Map<String, UUID>> getCurrentUserId(HttpSession session) {
		UUID userId = (UUID) session.getAttribute(USER_ID);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		Map<String, UUID> response = new HashMap<>();
		response.put(USER_ID, userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/current/user")
	public ResponseEntity<User> getCurrentUser(HttpSession session) {
		UUID userId = (UUID) session.getAttribute(USER_ID);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		Optional<User> currentUser = userService.getUserById(userId);
		if (currentUser.isEmpty())
			return ResponseEntity.noContent().build();
		else {
			User user = currentUser.get();
			user.setPassword("");
			return ResponseEntity.ok(user);
		}

	}
}

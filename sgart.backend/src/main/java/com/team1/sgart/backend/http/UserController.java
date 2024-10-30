package com.team1.sgart.backend.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.Admin;
import com.team1.sgart.backend.model.AdminDTO;
import com.team1.sgart.backend.model.GenericUser;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.UserDTO;
import com.team1.sgart.backend.services.UserService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {

	private UserService userService;

	@Autowired
	UserController(UserService userService) {
		this.userService = userService;
	}

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

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody User user) {
		GenericUser genericUser = userService.loginUser(user);
		Map<String, Object> response = new HashMap<>();

		if (genericUser instanceof User) {
			User userLogged = (User) genericUser;
			UserDTO userDTO = new UserDTO();
			userDTO.setID(userLogged.getID());
			userDTO.setName(userLogged.getName());
			userDTO.setLastName(userLogged.getLastName());
			userDTO.setEmail(userLogged.getEmail());
			userDTO.setPassword(userLogged.getPassword());
			userDTO.setDepartment(userLogged.getDepartment());
			userDTO.setCenter(userLogged.getCenter());
			userDTO.setHiringDate(userLogged.getHiringDate());
			userDTO.setProfile(userLogged.getProfile());
			userDTO.setValidated(userLogged.isValidated());
			userDTO.setBlocked(userLogged.isBlocked());

			response.put("user", userDTO);
			response.put("type", "user");

			return ResponseEntity.ok(response);
		}

		else if (genericUser instanceof Admin) {
			Admin adminLogged = (Admin) genericUser;
			AdminDTO adminDTO = new AdminDTO();
			adminDTO.setID(adminLogged.getID());
			adminDTO.setName(adminLogged.getName());
			adminDTO.setLastName(adminLogged.getLastName());
			adminDTO.setEmail(adminLogged.getEmail());
			adminDTO.setPassword(adminLogged.getPassword());

			response.put("admin", adminDTO);
			response.put("type", "admin");

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
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
}

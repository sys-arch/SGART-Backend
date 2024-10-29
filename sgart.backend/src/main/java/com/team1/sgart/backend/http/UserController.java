package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.User;
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
		return ResponseEntity.status(HttpStatus.OK).body("Usuario modificado correctamente");
	}
    
    @PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {

		userService.loginUser(user);
		return ResponseEntity.status(HttpStatus.OK).body("Usuario logueado correctamente"); //Meter también el JSON del usuario
	}
}

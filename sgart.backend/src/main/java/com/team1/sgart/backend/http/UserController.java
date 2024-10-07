package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.services.UserService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/registro")
	public void registrarUser() {
		userService.registrarUser(null);
	}
	
	//Aquí haremos los comprobadores de formato y tratamiento de entrada desde el Frontend
	public void comprobarPassword() {
		//Comprobador de dualidad de contraseña
		//Comprobador de formato de contraseña
	}
}

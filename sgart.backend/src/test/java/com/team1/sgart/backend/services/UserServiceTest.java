package com.team1.sgart.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.services.UserService;

public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testRegistrarUser() {
	    // Dado: Un usuario de prueba
	    User testUser = new User();
	    testUser.setName("Carlos");
	    testUser.setLastName("González");
	    testUser.setEmail("carlos@example.com");
	    testUser.setBlocked(false);
	    testUser.setCenter("Centro de prueba");
	    testUser.setDepartment("Departamento de prueba");
	    testUser.setHiringDate("01/01/2024");
	    testUser.setInternal(true);
	    testUser.setPassword("password");
	    testUser.setPasswordConfirm("password");
	    testUser.setProfile("profile");
	    
	    // Cuando: Se registra un usuario
	    userService.registrarUser(testUser);
	    
	    //Se comprobaría si el usuario ha sido introducido en la BBDD. 
	    //No implementado por dependencia de la implementación se esta función
	}
}



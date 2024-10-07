package com.team1.sgart.backend.services;

import org.springframework.stereotype.Service;

import com.team1.sgart.backend.model.User;

@Service
public class UserService {
	
	public void registrarUser(User user) {
		System.out.println("Usuario registrado");
	}
	
}

package com.team1.sgart.backend.services;

import com.team1.sgart.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.dao.AdminDao;
import com.team1.sgart.backend.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

	
	private UserDao userDao;
	private AdminDao adminDao;
	
	@Autowired
	UserService(UserDao userDao, AdminDao adminDao) {
        this.userDao = userDao;
        this.adminDao = adminDao;
    }

	public User registrarUser(User user) {
		// Comprobar si el email ya está registrado
		if (emailYaRegistrado(user)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
		} else if (!emailFormatoValido(user)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto");
		} else if (!passwordFormatoValido(user)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de la contraseña incorrecto");
		} else {
			userDao.save(user);
			return user;
		}
	}
	
	public void modificarUser(User user) {
		String email = user.getEmail();
		if(!emailYaRegistrado(user)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El email no está registrado");
		}else if (!emailFormatoValido(user)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto");
		} else {
			userDao.updateUser(email, user);
		}
	}
	
	public GenericUser loginUser(User user) {
		String email = user.getEmail();
		String password = user.getPassword();
		
		if(!emailFormatoValido(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto");
        }
		else if (adminDao.findByEmailAndPassword(email, password).isPresent())//Admin dao, buscar por email y password
        {
            Admin admin = adminDao.findByEmail(user.getEmail()).get();
            return admin;
        }
		else if (userDao.findByEmailAndPassword(email, password).isPresent()) {
			user = userDao.findByEmail(user.getEmail()).get();
			return user;
		}
		else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
		}
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
			userAbsenceDTO.setEmail(user.getEmail());
			userAbsenceDTO.setFirstName(user.getName());
			userAbsenceDTO.setLastName(user.getLastName());
			userAbsenceDTO.setCenter(user.getCenter());
			userAbsenceDTO.setProfile(user.getProfile());
			return userAbsenceDTO;
		}).collect(Collectors.toList());
	}
}
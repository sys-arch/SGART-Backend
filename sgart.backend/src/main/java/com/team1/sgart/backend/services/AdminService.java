package com.team1.sgart.backend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.dao.AdminDao;
import com.team1.sgart.backend.dao.InvitationsDao;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.Admin;
import com.team1.sgart.backend.model.AdminDTO;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.UserDTO;

@Service
public class AdminService {
	
	private UserDao userDAO;
	private AdminDao adminDAO;
	private InvitationsDao invitationsDAO;
	
	@Autowired
	AdminService(UserDao userDao, AdminDao adminDao,InvitationsDao invitationsDAO) {
        this.userDAO = userDao;
        this.adminDAO = adminDao;
        this.invitationsDAO=invitationsDAO;
    }
	
	public List<UserDTO> mapUser(List<User> users){
		return users.stream().map(user -> {
			UserDTO dto= new UserDTO();
			dto.setID(user.getID());
			dto.setEmail(user.getEmail());
			dto.setName(user.getName());
			dto.setLastName(user.getLastName());
			dto.setBlocked(user.isBlocked());
			dto.setValidated(user.isValidated());
			dto.setCenter(user.getCenter());
			dto.setProfile(user.getProfile());
			dto.setDepartment(user.getDepartment());
			dto.setHiringDate(user.getHiringDate());
			return dto;
		}).collect(Collectors.toList());
	}
	
	public List<AdminDTO> mapAdmin(List<Admin> admins){
		return admins.stream().map(admin -> {
			AdminDTO dto= new AdminDTO();
			dto.setID(admin.getID());
			dto.setEmail(admin.getEmail());
			dto.setName(admin.getName());
			dto.setLastName(admin.getLastName());
			dto.setCenter(admin.getCenter());
			dto.setBlocked(admin.getBlocked());
			return dto;
		}).collect(Collectors.toList());
	}
	
	public List<UserDTO> getUsuariosValidados() {
		List<User> users= userDAO.getUsuariosValidados().get();
		return mapUser(users);
	}
	
	public List<UserDTO> getUsuariosSinValidar() {
		List<User> users= userDAO.getUsuariosSinValidar().get();
		return mapUser(users);
	}

	public void validarUsuario(String userEmail) {
		// Buscar al usuario por email
		User user = userDAO.findByEmail(userEmail)
				.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

		// Validar al usuario si no está validado
		if (!isUserValidated(user)) {
			userDAO.validarUsuario(userEmail);
		} else {
			throw new IllegalArgumentException("El usuario ya está validado");
		}
	}
	
	public void cambiarHabilitacionUsuario(String userEmail){
		if(!emailUserEstaRegistrado(userEmail)) {
			if(!emailAdminEstaRegistrado(userEmail))
				throw new IllegalArgumentException();
			else
				adminDAO.cambiarHabilitacionAdmin(userEmail);
		} else
			userDAO.cambiarHabilitacionUsuario(userEmail);
    }

	private boolean isUserValidated(User user) {
		// Verificar si el usuario ya está validado en la base de datos
		return userDAO.isUsuarioValidado(user.getEmail());
	}

    public void eliminarUsuarioPorEmail(String email) {
    	if(!emailUserEstaRegistrado(email)) {
			if(!emailAdminEstaRegistrado(email))
				throw new IllegalArgumentException();
			else
				adminDAO.deleteByEmail(email);
		} else {
			User userToDelete=userDAO.findByEmail(email).get();
			invitationsDAO.deleteByUser(userToDelete.getID());
			userDAO.deleteByEmail(email);
		}
    }

	public List<AdminDTO> getAdmins() {
		List<Admin> admins= adminDAO.findAll();
		return mapAdmin(admins);
	}

	public void modificarAdmin(Admin admin) {
		String email = admin.getEmail();
		if(!emailAdminEstaRegistrado(email)) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El email no está registrado");
		adminDAO.updateAdmin(email, admin);
	}
	
	public UUID crearAdmin(Admin admin) {
		String email = admin.getEmail();
		if(emailAdminEstaRegistrado(email)) 
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email ya está registrado");
		adminDAO.save(admin);
		return adminDAO.findByEmail(admin.getEmail()).get().getID();
	}
	
	private boolean emailUserEstaRegistrado(String email) {
		return userDAO.findByEmail(email).isPresent();
	}
	public boolean emailAdminEstaRegistrado(String email) {
		return adminDAO.findByEmail(email).isPresent();
	}
}
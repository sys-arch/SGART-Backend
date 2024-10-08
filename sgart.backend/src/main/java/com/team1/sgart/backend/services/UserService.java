package com.team1.sgart.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team1.sgart.backend.dao.UserDAO;
import com.team1.sgart.backend.http.RegistrationRequest;
import com.team1.sgart.backend.model.User;

@Service
public class UserService {

    
    private UserDAO userDAO;

    public User registrarUsuario(RegistrationRequest request) {
        if (userDAO.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        User usuario = new User();
        usuario.setName(request.getName());
        usuario.setLastName(request.getLastName());
        usuario.setEmail(request.getEmail());
        usuario.setDepartment(request.getDepartment());
        usuario.setCenter(request.getCenter());
        usuario.setHiringDate(Date.from(request.getHiringDate().toInstant()));
        usuario.setProfile(request.getProfile());
        usuario.setPassword(request.getPassword());

        return userDAO.save(usuario);
    }

    public void validarUsuario(String email) {
    	 if (!userDAO.isUsuarioValidado(email)) {
             userDAO.validarUsuario(email);
         } else {
             throw new IllegalArgumentException("El usuario ya está validado");
         }
		

    }
}


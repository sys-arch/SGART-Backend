package com.team1.sgart.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team1.sgart.backend.dao.UserDAO;
import com.team1.sgart.backend.http.RegistrationRequest;
import com.team1.sgart.backend.model.User;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public User registrarUsuario(RegistrationRequest request) {
        if (userDAO.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setCenter(request.getCenter());
        user.setHiringDate(Date.from(request.getHiringDate().toInstant()));
        user.setProfile(request.getProfile());
        user.setPassword(request.getPassword()); 

        return userDAO.save(user);
    }
}

package com.team1.sgart.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team1.sgart.backend.dao.AdminDao;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.Admin;
import com.team1.sgart.backend.model.User;

@Service
public class ValidationService {

    private final UserDao userDao;
    private final AdminDao adminDao;

    @Autowired
    public ValidationService(UserDao userDao, AdminDao adminDao) {
        this.userDao = userDao;
        this.adminDao = adminDao;
    }

    
    //Verifica si un email ya está registrado en el sistema, ya sea en la tabla de usuarios o administradores.
    
    public boolean emailExisteEnSistema(String email) {
        boolean registradoEnSistema = false;

        // Comprobar si el correo está registrado como usuario
        Optional<User> usuarioExistente = userDao.findByEmail(email.toLowerCase());
        if (usuarioExistente.isPresent()) {
            registradoEnSistema = true;
        }

        // Comprobar si el correo está registrado como administrador
        Optional<Admin> adminExistente = adminDao.findByEmail(email.toLowerCase());
        if (adminExistente.isPresent()) {
            registradoEnSistema = true;
        }

        return registradoEnSistema;
    }
}
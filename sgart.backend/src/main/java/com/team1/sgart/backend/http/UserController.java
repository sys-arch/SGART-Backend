package com.team1.sgart.backend.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.services.UserService;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<User> registrarUsuario(@RequestBody RegistrationRequest request) {
        User user = usuarioService.registrarUsuario(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
/*	lo hace admincontroller
    @PutMapping("/validar/{email}")
    public ResponseEntity<String> validarUsuario(@PathVariable String email) {
        usuarioService.validarUsuario(email);
        return new ResponseEntity<>("Usuario validado con éxito", HttpStatus.OK);
    }
}
*/
}


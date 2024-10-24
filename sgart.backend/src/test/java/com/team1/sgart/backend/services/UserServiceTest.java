package com.team1.sgart.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.User;

@SpringBootTest
class UserServiceTest {
	
	private static final String PASSWORD_DEBIL = "password";
	private static final String NUEVO_NOMBRE = "Pablo"; 

    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Carlos", "Romero Navarro", "Quality", "Ciudad Real", "carlos.romero@example.com", "01/01/2024", 
                        "Scrum Developer", "Password123@", "Password123@", false);
        
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.empty()); // Email no registrado
        Mockito.when(userDao.save(user)).thenReturn(user); // Simular guardado de usuario
    }

    @Test
    void testRegistrarUserExitoso() {
        User resultado = userService.registrarUser(user);
        assertEquals(user, resultado);  // Verificar que se devuelve el mismo usuario
    }

    @Test
    void testRegistrarUserEmailYaRegistrado() {
    	
    	Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(optionalUser); // Simular que el email ya está registrado
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            userService.registrarUser(user)
        );
        assertEquals("El email ya está registrado", exception.getReason());
    }

    @Test
    void testRegistrarUserPasswordDebil() {
    	
        user.setPassword(PASSWORD_DEBIL); // Contraseña débil
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            userService.registrarUser(user)
        );

        assertEquals("Formato de la contraseña incorrecto", exception.getReason());
    }

    @Test
    void testRegistrarUserEmailFormatoInvalido() {
        user.setEmail("carlos.romero"); // Email con formato inválido
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            userService.registrarUser(user)
        );

        assertEquals("Formato del email incorrecto", exception.getReason());
    }
    
    @Test
    void testModificarUserExistente() {
                
        Optional<User> optionalUser = Optional.of(user);
        
        User userModificado = new User();
        userModificado.setName(NUEVO_NOMBRE);
        userModificado.setEmail("carlos.romero@example.com");

        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(optionalUser); // Simular que el usuario existe
        Mockito.when(userDao.save(userModificado)).thenReturn(userModificado);

        userService.modificarUser(userModificado);
        assertEquals(NUEVO_NOMBRE, userModificado.getName()); // Verificar que el perfil ha sido actualizado
    }

    @Test
    void testModificarUserNoExistente() {
    	User userModificado = new User();
        userModificado.setName(NUEVO_NOMBRE);
        userModificado.setEmail("romero@example.com");

        assertThrows(ResponseStatusException.class, () -> 
            userService.modificarUser(userModificado)
        );
    }

}

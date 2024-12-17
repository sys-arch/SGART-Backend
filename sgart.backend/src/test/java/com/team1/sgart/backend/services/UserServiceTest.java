package com.team1.sgart.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.dao.AdminDao;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.Admin;
import com.team1.sgart.backend.model.GenericUser;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.UserDTO;
import com.team1.sgart.backend.util.JwtTokenProvider;

@SpringBootTest
class UserServiceTest {
    
    private static final String PASSWORD_DEBIL = "password";
    private static final String NUEVO_NOMBRE = "Pablo";
    private static final String PASSWORD_FUERTE = "Password365@";
    private static final String FECHA_CORRECTA = "01/01/2024";
    private static final String PASSWORD_INCORRECTO = "wrongPassword";
    private static final String EMAIL_USUARIO = "carlos.romero@example.com";
    private static final String EMAIL_ADMIN = "admin@example.com";
    private static final String DEPARTAMENTO = "Quality";

    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;
    
    @MockBean
    private AdminDao adminDao;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private User user;
    private Admin admin;
    private UserDTO jsonPerfilModificado = new UserDTO();

    @BeforeEach
    void setUp() {
        user = new User(EMAIL_USUARIO, "Carlos", "Romero Navarro", DEPARTAMENTO, "Ciudad Real", FECHA_CORRECTA, 
                "Scrum Developer", PASSWORD_FUERTE, PASSWORD_FUERTE, false, false, "");
        
        admin = new Admin("admin", "example", EMAIL_ADMIN, PASSWORD_FUERTE);
        
        jsonPerfilModificado.setID(user.getID());
        jsonPerfilModificado.setName("John");
        jsonPerfilModificado.setLastName("Marston");
        jsonPerfilModificado.setDepartment(DEPARTAMENTO);
        jsonPerfilModificado.setCenter("Royal City");
        jsonPerfilModificado.setProfile("Scrum Master");
        
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
    void testModificarUserExistente() {
        // Datos de prueba
        Optional<User> optionalUser = Optional.of(user);

        User userModificado = new User();
        userModificado.setEmail(EMAIL_USUARIO);
        userModificado.setName(NUEVO_NOMBRE);

        // Configurar los mocks
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(optionalUser); // Simular que el usuario existe
        Mockito.when(userDao.save(Mockito.any(User.class))).thenReturn(userModificado); // Simular guardado de usuario

        // Ejecutar el método
        userService.modificarUser(userModificado);

        // Verificar que el perfil ha sido actualizado
        assertEquals(NUEVO_NOMBRE, user.getName());
        Mockito.verify(userDao).save(user);
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
    
    
    @Test
    void testLoginUsuarioNoEncontradoDevuelve401() {
        Mockito.when(userDao.findByEmailAndPassword(EMAIL_USUARIO, PASSWORD_INCORRECTO)).thenReturn(Optional.empty());
        Mockito.when(adminDao.findByEmailAndPassword(EMAIL_USUARIO, PASSWORD_INCORRECTO)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            userService.loginUser(user, new MockHttpSession())
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Email no encontrado.", exception.getReason());
    }

    @Test
    void testLoginEmailFormatoInvalidoDevuelve400() {
        user.setEmail("carlos.romero");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            userService.loginUser(user, new MockHttpSession())
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Formato del email incorrecto", exception.getReason());
    }
    
    @Test
    void testModificarPerfilUserExistente() {
        Optional<User> optionalUser = Optional.of(user);

        User userPerfilModificado = new User();
        userPerfilModificado.setID(user.getID());
        userPerfilModificado.setName(jsonPerfilModificado.getName());
        userPerfilModificado.setLastName(jsonPerfilModificado.getLastName());
        userPerfilModificado.setDepartment(jsonPerfilModificado.getDepartment());
        userPerfilModificado.setCenter(jsonPerfilModificado.getCenter());
        userPerfilModificado.setProfile(jsonPerfilModificado.getProfile());

        Mockito.when(userDao.findById(user.getID())).thenReturn(optionalUser); // Simular que el usuario existe
        Mockito.when(userDao.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simular guardado de usuario

        userService.modificarPerfilUser(jsonPerfilModificado);

        assertEquals("John", user.getName());
        assertEquals("Marston", user.getLastName());
        assertEquals(DEPARTAMENTO, user.getDepartment());
        assertEquals("Royal City", user.getCenter());
        assertEquals("Scrum Master", user.getProfile());
        Mockito.verify(userDao).save(user); 
    }

    @Test
    void testModificarPerfilUserNoExistente() {
        User userModificado = new User();
        userModificado.setID(UUID.randomUUID());
        userModificado.setName(NUEVO_NOMBRE);
        userModificado.setEmail("romero@example.com");

        assertThrows(ResponseStatusException.class, () -> 
            userService.modificarUser(userModificado)
        );
    }

}
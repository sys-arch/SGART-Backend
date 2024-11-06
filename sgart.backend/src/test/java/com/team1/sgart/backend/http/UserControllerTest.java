package com.team1.sgart.backend.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
	
	private static final String URLREGISTRAR = "/users/registro";
	private static final String URLMODIFICAR = "/users/modificar";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("carlos.romero@example.com", "Carlos", "Romero Navarro", "Quality", "Ciudad Real", "01/01/2024", 
                        "Scrum Developer", "password123@", "password123@", false, false, "");
    }

    @Test
    void usuarioValidoRegistroDevuelve201() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.when(userService.registrarUser(user)).thenReturn(user);
        mockMvc.perform(post(URLREGISTRAR).contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    void emailUsuarioInvalidoRegistroDevuelve400() throws Exception {
        user.setEmail("carlos.romero");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto"))
                .when(userService).registrarUser(Mockito.any(User.class));
        
        mockMvc.perform(post(URLREGISTRAR).contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usuarioExistenteRegistroDevuelve409() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado"))
                .when(userService).registrarUser(Mockito.any(User.class));
        
        mockMvc.perform(post(URLREGISTRAR)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isConflict());
    }

    @Test
    void passwordDebilRegistroDevuelve400() throws Exception {
        user.setPassword("password");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de la contraseña incorrecto"))
                .when(userService).registrarUser(Mockito.any(User.class));
        
        mockMvc.perform(post(URLREGISTRAR)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    // Tests para el método modificar
    @Test
    void usuarioValidoModificarDevuelve200() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doNothing().when(userService).modificarUser(user);
        mockMvc.perform(post(URLMODIFICAR).contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario modificado correctamente"));
    }

    @Test
    void emailUsuarioInvalidoModificarDevuelve400() throws Exception {
        user.setEmail("carlos.romero");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto"))
                .when(userService).modificarUser(Mockito.any(User.class));
        
        mockMvc.perform(post(URLMODIFICAR).contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usuarioNoExistenteModificarDevuelve404() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"))
                .when(userService).modificarUser(Mockito.any(User.class));
        
        mockMvc.perform(post(URLMODIFICAR).contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isNotFound());
    }
}

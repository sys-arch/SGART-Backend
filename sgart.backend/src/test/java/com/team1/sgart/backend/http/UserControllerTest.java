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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Carlos", "Romero Navarro", "Quality", "Ciudad Real", "carlos.romero@example.com", "01/01/2024", 
                        "Scrum Developer", "password123@", "password123@", false);
    }

    @Test
    void usuarioValido_registro_devuelve201() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.when(userService.registrarUser(user)).thenReturn(user);
        mockMvc.perform(post("/users/registro").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    void emailUsuarioInvalido_registro_devuelve400() throws Exception {
        user.setEmail("carlos.romero");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto"))
                .when(userService).registrarUser(Mockito.any(User.class));
        
        mockMvc.perform(post("/users/registro").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usuarioExistente_registro_devuelve409() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado"))
                .when(userService).registrarUser(Mockito.any(User.class));
        
        mockMvc.perform(post("/users/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isConflict());
    }

    @Test
    void passwordDebil_registro_devuelve400() throws Exception {
        user.setPassword("password");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de la contraseña incorrecto"))
                .when(userService).registrarUser(Mockito.any(User.class));
        
        mockMvc.perform(post("/users/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    // Tests para el método modificar
    @Test
    void usuarioValido_modificar_devuelve200() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doNothing().when(userService).modificarUser(user);
        mockMvc.perform(post("/users/modificar").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario modificado correctamente"));
    }

    @Test
    void emailUsuarioInvalido_modificar_devuelve400() throws Exception {
        user.setEmail("carlos.romero");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del email incorrecto"))
                .when(userService).modificarUser(Mockito.any(User.class));
        
        mockMvc.perform(post("/users/modificar").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usuarioNoExistente_modificar_devuelve404() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"))
                .when(userService).modificarUser(Mockito.any(User.class));
        
        mockMvc.perform(post("/users/modificar").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isNotFound());
    }
}

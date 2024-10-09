package com.team1.sgart.backend.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {UserController.class, UserService.class})
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    

    User user;
    @BeforeEach
    public void setUp() {
        user = new User("Carlos", "Romero Navarro", "Quality", "Ciudad Real", "carlos.romero@example.com", "01/01/2024", 
                        "Scrum Developer", "password123@", "password123@", true, false);
    }

    @Test
    public void usuarioValido_devuelve201() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario registrado correctamente"));
    }

    @Test
    public void emailUsuarioInvalido_devuelve400() throws Exception {
        
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Formato del email incorrecto"));
    }

    @Test
    public void usuarioExistente_devuelve409() throws Exception {
       when(userService.emailYaRegistrado(user.getEmail())).thenReturn(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email en uso")); //Sujeto a cambios de seguridad
    }

    @Test
    public void passwordDebil_devuelve400() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").value("Siga las instrucciones de seguridad para la contraseña"));
    }

    @Test
    public void jsonMalformado_devuelve400() throws Exception {
        String malformedJson = "{ \"name\": \"John\", \"lastName\": \"Doe\" "; // JSON malformado

        mockMvc.perform(post("/users/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }
}

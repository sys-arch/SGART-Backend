package com.team1.sgart.backend.services;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.team1.sgart.backend.dao.InvitationsDao;
import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.User;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

	@Mock
	private UserDao userDAO;
	
	@Mock
	private InvitationsDao invitationsDAO;

	@InjectMocks
	private AdminService adminService;

	@Test
	public void testValidarUsuario_UserNotFound() {
		// Simular que el usuario no existe
		String userEmail = "user@dominio.com";
		Mockito.when(userDAO.findByEmail(userEmail)).thenReturn(Optional.empty());

		// Ejecutar el test y esperar una excepción
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			adminService.validarUsuario(userEmail);
		});

		// Verificar el mensaje de la excepción
		assertEquals("Usuario no encontrado", exception.getMessage());
	}

	@Test
	public void testValidarUsuario_UsuarioYaValidado() {
		// Simular que el usuario existe y ya está validado
		String userEmail = "user@dominio.com";
		User user = new User();
		user.setEmail(userEmail);
		Mockito.when(userDAO.findByEmail(userEmail)).thenReturn(Optional.of(user));
		Mockito.when(userDAO.isUsuarioValidado(userEmail)).thenReturn(true);

		// Ejecutar el test y esperar una excepción
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			adminService.validarUsuario(userEmail);
		});

		// Verificar el mensaje de la excepción
		assertEquals("El usuario ya está validado", exception.getMessage());
	}

	@Test
	public void testValidarUsuario_Exito() {
		// Simular que el usuario existe y no está validado
		String userEmail = "user@dominio.com";
		User user = new User();
		user.setEmail(userEmail);
		Mockito.when(userDAO.findByEmail(userEmail)).thenReturn(Optional.of(user));
		Mockito.when(userDAO.isUsuarioValidado(userEmail)).thenReturn(false);

		// Ejecutar el método
		adminService.validarUsuario(userEmail);

		// Verificar que el método de validación fue llamado
		Mockito.verify(userDAO, Mockito.times(1)).validarUsuario(userEmail);
	}


	/*
	 * EN FRONT SE PASA ID O MAIL QUE SÍ EXISTE
	 * @Test public void testEliminarUsuarioPorEmail_UsuarioNoExiste() { String
	 * userEmail = "nonexistent@example.com";
	 * lenient().when(userDAO.findByEmail(userEmail)).thenReturn(Optional.empty());
	 * adminService.eliminarUsuarioPorEmail(userEmail); verify(userDAO,
	 * times(0)).deleteByEmail(userEmail); }
	 */

	@Test
	public void testEliminarUsuarioPorEmail_UsuarioExiste() {
		String userEmail = "test@example.com";
		lenient().when(userDAO.findByEmail(userEmail)).thenReturn(Optional.of(new User()));

		adminService.eliminarUsuarioPorEmail(userEmail);

		verify(userDAO, times(1)).deleteByEmail(userEmail);
	}

	@Test
    public void testBloquearUsuario_UsuarioNoBloqueado() {
        String userEmail = "test@example.com";
        User user = new User();
        user.setBlocked(false); // Usuario inicialmente no bloqueado

        lenient().when(userDAO.findByEmail(userEmail)).thenReturn(Optional.of(user));

        adminService.cambiarHabilitacionUsuario(userEmail); // Cambia a bloqueado

        // Verificar que se llama a cambiarHabilitacionUsuario en el DAO
        verify(userDAO, times(1)).cambiarHabilitacionUsuario(userEmail);
    }

    @Test
    public void testDesbloquearUsuario_UsuarioBloqueado() {
        String userEmail = "test@example.com";
        User user = new User();
        user.setBlocked(true); // Usuario inicialmente bloqueado

        lenient().when(userDAO.findByEmail(userEmail)).thenReturn(Optional.of(user));

        adminService.cambiarHabilitacionUsuario(userEmail); // Cambia a desbloqueado

        // Verificar que se llama a cambiarHabilitacionUsuario en el DAO
        verify(userDAO, times(1)).cambiarHabilitacionUsuario(userEmail);
    }
}


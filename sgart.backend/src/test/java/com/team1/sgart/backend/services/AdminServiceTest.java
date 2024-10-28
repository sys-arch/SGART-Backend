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
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.team1.sgart.backend.dao.UserDao;
import com.team1.sgart.backend.model.User;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

	@Mock
	private UserDao userDAO;

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

	@Test
	public void testEliminarUsuarioPorId_UsuarioExiste() {
		Integer userId = 1;
		lenient().when(userDAO.findById(userId)).thenReturn(Optional.of(new User()));

		adminService.eliminarUsuarioPorId(userId);

		verify(userDAO, times(1)).deleteById(userId);
	}

	/*
	 * EN FRONT SE PASA ID O MAIL QUE SÍ EXISTE
	 * 
	 * @Test public void testEliminarUsuarioPorId_UsuarioNoExiste() { Integer userId
	 * = 999; lenient().when(userDAO.findById(userId)).thenReturn(Optional.empty());
	 * 
	 * adminService.eliminarUsuarioPorId(userId);
	 * 
	 * verify(userDAO, times(0)).deleteById(userId); }
	 */

	/*
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

}

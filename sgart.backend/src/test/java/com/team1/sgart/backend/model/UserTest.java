package com.team1.sgart.backend.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

	private static final String PASSWORD_FUERTE = "Password365#@";
	private static final String FECHA_CORRECTA = "01/01/2024";
	
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("carlos.romero@example.com", "Carlos", "Romero Navarro", "Quality", "Ciudad Real", FECHA_CORRECTA, 
                        "Scrum Developer", PASSWORD_FUERTE, PASSWORD_FUERTE, false, false, "");
    }

    @Test
    void testConstructor() {
        assertEquals("Carlos", user.getName());
        assertEquals("Romero Navarro", user.getLastName());
        assertEquals("Quality", user.getDepartment());
        assertEquals("Ciudad Real", user.getCenter());
        assertEquals("carlos.romero@example.com", user.getEmail());
        assertEquals(FECHA_CORRECTA, user.getHiringDate());
        assertEquals("Scrum Developer", user.getProfile());
        assertEquals(PASSWORD_FUERTE, user.getPassword());
        assertEquals(PASSWORD_FUERTE, user.getPasswordConfirm());
        assertFalse(user.isBlocked());
    }

    @Test
    void testSetName() {
        user.setName("Juan");
        assertEquals("Juan", user.getName());
    }

    @Test
    void testSetLastName() {
        user.setLastName("Garcia");
        assertEquals("Garcia", user.getLastName());
    }

    @Test
    void testPasswordConfirmationMatches() {
        assertEquals(user.getPassword(), user.getPasswordConfirm(), 
                     "Password and passwordConfirm should match");
    }

    @Test
    void testBlockUser() {
        user.setBlocked(true);
        assertTrue(user.isBlocked());
    }

    @Test
    void testSetEmail() {
        user.setEmail("new.email@example.com");
        assertEquals("new.email@example.com", user.getEmail());
    }

    @Test
    void testSetProfile() {
        user.setProfile("User");
        assertEquals("User", user.getProfile());
    }
    
    @Test
	void testSetPassword() {
		user.setPassword("newPassword123");
		assertEquals("newPassword123", user.getPassword());
	}
    

    
    @Test
    void testComprobarFormatoEmailCorrecto() {
    	user.setEmail("user@example.com");
    	assertTrue(user.comprobarFormatoEmail());
    }
    
    @Test 
    void testComprobarFormatoEmailIncorrecto() {
    	user.setEmail("userexample.com");
    	assertFalse(user.comprobarFormatoEmail());
    }
    
    @Test
	void testComprobarFormatoFechaCorrecto() {
		user.setHiringDate(FECHA_CORRECTA);
		assertTrue(user.comprobarFormatoFecha());
	}
    
    @Test
    void testComprobarFormatoFechaIncorrecto() {
    	user.setHiringDate("01-01-24");
    	assertFalse(user.comprobarFormatoFecha());
    }
}

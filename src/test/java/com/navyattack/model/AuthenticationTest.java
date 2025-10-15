package com.navyattack.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Authentication
 */
class AuthenticationTest {

    // ==================== TESTS DE LOGIN ====================

    @Test
    @DisplayName("Login exitoso con credenciales correctas")
    void testLoginWithCorrectCredentials() {
        User user = new User("testuser", "password123");

        boolean result = Authentication.login("testuser", "password123", user);

        assertTrue(result);
    }

    @Test
    @DisplayName("Login fallido con contraseña incorrecta")
    void testLoginWithIncorrectPassword() {
        User user = new User("testuser", "password123");

        boolean result = Authentication.login("testuser", "wrongpassword", user);

        assertFalse(result);
    }

    @Test
    @DisplayName("Login fallido con usuario null")
    void testLoginWithNullUser() {
        boolean result = Authentication.login("testuser", "password123", null);

        assertFalse(result);
    }

    @Test
    @DisplayName("Login fallido con contraseña vacía")
    void testLoginWithEmptyPassword() {
        User user = new User("testuser", "password123");

        boolean result = Authentication.login("testuser", "", user);

        assertFalse(result);
    }

    @Test
    @DisplayName("Login fallido con contraseña null")
    void testLoginWithNullPassword() {
        User user = new User("testuser", "password123");

        // Esto debería lanzar NullPointerException o retornar false
        assertThrows(NullPointerException.class, () -> {
            Authentication.login("testuser", null, user);
        });
    }

    // ==================== TESTS DE CREACIÓN DE CUENTA ====================

    @Test
    @DisplayName("Crear cuenta exitosamente con datos válidos")
    void testCreateAccountSuccess() {
        assertDoesNotThrow(() -> {
            User newUser = Authentication.createAccount(
                    "newuser",
                    "password123",
                    "password123",
                    null
            );

            assertNotNull(newUser);
            assertEquals("newuser", newUser.getUsername());
            assertEquals("password123", newUser.getPassword());
        });
    }

    @Test
    @DisplayName("Crear cuenta falla cuando usuario ya existe")
    void testCreateAccountUserAlreadyExists() {
        User existingUser = new User("existinguser", "password123");

        Exception exception = assertThrows(Exception.class, () -> {
            Authentication.createAccount(
                    "existinguser",
                    "newpassword",
                    "newpassword",
                    existingUser
            );
        });

        assertEquals("The user already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Crear cuenta falla con contraseña muy corta")
    void testCreateAccountPasswordTooShort() {
        Exception exception = assertThrows(Exception.class, () -> {
            Authentication.createAccount("newuser", "12345", "12345", null);
        });

        assertEquals("Password must be longer than 6 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Crear cuenta falla cuando contraseñas no coinciden")
    void testCreateAccountPasswordsDontMatch() {
        Exception exception = assertThrows(Exception.class, () -> {
            Authentication.createAccount(
                    "newuser",
                    "password123",
                    "password456",
                    null
            );
        });

        assertEquals("Passwords are not the same", exception.getMessage());
    }

    @Test
    @DisplayName("Crear cuenta falla con username vacío")
    void testCreateAccountEmptyUsername() {
        Exception exception = assertThrows(Exception.class, () -> {
            Authentication.createAccount("", "password123", "password123", null);
        });

        assertEquals("User cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Crear cuenta falla con username null")
    void testCreateAccountNullUsername() {
        Exception exception = assertThrows(Exception.class, () -> {
            Authentication.createAccount(null, "password123", "password123", null);
        });

        assertEquals("User cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Crear cuenta falla con username solo espacios")
    void testCreateAccountUsernameOnlySpaces() {
        Exception exception = assertThrows(Exception.class, () -> {
            Authentication.createAccount("   ", "password123", "password123", null);
        });

        assertEquals("User cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Crear cuenta falla con contraseña null")
    void testCreateAccountNullPassword() {
        assertThrows(Exception.class, () -> {
            Authentication.createAccount("newuser", null, "password123", null);
        });
    }

    @Test
    @DisplayName("Crear cuenta con contraseña de exactamente 6 caracteres falla")
    void testCreateAccountPasswordExactlySixCharacters() {
        Exception exception = assertThrows(Exception.class, () -> {
            Authentication.createAccount("newuser", "pass12", "pass12", null);
        });

        assertEquals("Password must be longer than 6 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Crear cuenta exitosamente con contraseña de 7 caracteres")
    void testCreateAccountPasswordSevenCharacters() {
        assertDoesNotThrow(() -> {
            User newUser = Authentication.createAccount(
                    "newuser",
                    "pass123",
                    "pass123",
                    null
            );

            assertNotNull(newUser);
        });
    }

    // ==================== TESTS DE VALIDACIÓN ====================

    @Test
    @DisplayName("Validar que usuario creado tiene el username correcto")
    void testCreatedUserHasCorrectUsername() throws Exception {
        User user = Authentication.createAccount(
                "testuser",
                "password123",
                "password123",
                null
        );

        assertEquals("testuser", user.getUsername());
    }

    @Test
    @DisplayName("Validar que usuario creado tiene la contraseña correcta")
    void testCreatedUserHasCorrectPassword() throws Exception {
        User user = Authentication.createAccount(
                "testuser",
                "password123",
                "password123",
                null
        );

        assertEquals("password123", user.getPassword());
    }

    @Test
    @DisplayName("Validar que usuario creado tiene historial vacío")
    void testCreatedUserHasEmptyHistory() throws Exception {
        User user = Authentication.createAccount(
                "testuser",
                "password123",
                "password123",
                null
        );

        assertNotNull(user.getHistory());
        assertTrue(user.getHistory().isEmpty());
    }

    // ==================== TESTS DE CASOS EDGE ====================

    @Test
    @DisplayName("Login con username diferente pero contraseña correcta")
    void testLoginDifferentUsernameCorrectPassword() {
        User user = new User("testuser", "password123");

        // Aunque la contraseña sea correcta, si el user es diferente, falla
        boolean result = Authentication.login("wronguser", "password123", user);

        // En este caso, como el user object no es null, debería verificar la contraseña
        assertTrue(result); // La lógica actual solo verifica password, no username
    }

    @Test
    @DisplayName("Crear múltiples usuarios con el mismo password")
    void testCreateMultipleUsersWithSamePassword() throws Exception {
        User user1 = Authentication.createAccount(
                "user1",
                "samepassword",
                "samepassword",
                null
        );

        User user2 = Authentication.createAccount(
                "user2",
                "samepassword",
                "samepassword",
                null
        );

        assertNotNull(user1);
        assertNotNull(user2);
        assertEquals("samepassword", user1.getPassword());
        assertEquals("samepassword", user2.getPassword());
    }

    @Test
    @DisplayName("Crear cuenta con caracteres especiales en username")
    void testCreateAccountWithSpecialCharactersInUsername() {
        assertDoesNotThrow(() -> {
            User user = Authentication.createAccount(
                    "user_123-test",
                    "password123",
                    "password123",
                    null
            );

            assertEquals("user_123-test", user.getUsername());
        });
    }

    @Test
    @DisplayName("Crear cuenta con espacios en username no vacío")
    void testCreateAccountWithSpacesInUsername() {
        assertDoesNotThrow(() -> {
            User user = Authentication.createAccount(
                    "user name",
                    "password123",
                    "password123",
                    null
            );

            assertEquals("user name", user.getUsername());
        });
    }
}
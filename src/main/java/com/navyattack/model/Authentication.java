package com.navyattack.model;

/**
 * Clase de autenticación para el sistema de usuarios de NavyAttack.
 * Proporciona métodos estáticos para validar credenciales de inicio de sesión
 * y crear nuevas cuentas de usuario con validaciones apropiadas.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class Authentication {
    
    /**
     * Valida las credenciales de inicio de sesión de un usuario.
     * Verifica que el usuario exista, que la contraseña no esté vacía
     * y que coincida con la contraseña almacenada.
     * 
     * @param username Nombre de usuario (no utilizado en la validación actual)
     * @param password Contraseña proporcionada por el usuario
     * @param user Usuario a validar (debe existir en el sistema)
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public static boolean login(String username, String password, User user) {
        if (user == null || password.isEmpty()) {
            return false;
        }
        if (!user.getPassword().equals(password)) {
            	return false;
        }
        return true;
    }
    
    /**
     * Crea una nueva cuenta de usuario después de validar los datos proporcionados.
     * Verifica que el usuario no exista previamente, que el nombre de usuario no esté vacío,
     * que la contraseña cumpla los requisitos mínimos y que ambas contraseñas coincidan.
     * 
     * @param username Nombre de usuario deseado
     * @param password Contraseña para la nueva cuenta
     * @param passwordConfirm Confirmación de la contraseña
     * @param existingUser Usuario existente con el mismo nombre (debe ser null)
     * @return Usuario creado exitosamente
     * @throws Exception Si el usuario ya existe o los datos no son válidos
     */
    public static User createAccount(String username, String password, String passwordConfirm, User existingUser) throws Exception {
        if (existingUser instanceof User) {
            throw new Exception("The user already exists");
        }
	    validateUserData(username, password, passwordConfirm);
        
        User newUser = new User(username, password);
    	return newUser;
    }
    
    /**
     * Valida los datos proporcionados para crear una nueva cuenta de usuario.
     * Verifica que el nombre de usuario no esté vacío, que la contraseña tenga
     * al menos 6 caracteres y que ambas contraseñas coincidan.
     * 
     * @param username Nombre de usuario a validar
     * @param password Contraseña a validar
     * @param passwordConfirm Confirmación de contraseña a validar
     * @throws Exception Si alguna validación falla, con mensaje específico del error
     */
    private static void validateUserData(String username, String password, String passwordConfirm) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("User cannot be empty");
        }
        if (password == null || password.length() < 6) {
            throw new Exception("Password must be longer than 6 characters");
        }
	    if (!password.equals(passwordConfirm)) {
		    throw new Exception("Passwords are not the same");
	    }
    }
}
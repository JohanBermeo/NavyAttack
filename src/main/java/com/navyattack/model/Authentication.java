package com.navyattack.model;

import java.util.Date;
import java.util.ArrayList;

public class Authentication {
    
    public boolean login(String username, String password, User user) {
	if (user == null || password.isEmpty()) {
		return false;
	}
        if (!user.getPassword().equals(password)) {
            	return false;
        }
        return true;
    }
    
    public void createAccount(String username, String password, Date birthday) throws Exception {
        validateUserData(username, password, birthday);

	// Esto lo debe hacer el controller
        //boolean existingUser = userController.existsById(username.hashCode());
        
        //if (existingUser) {
            //throw new Exception("El usuario ya existe");
        //}
        
        //User newUser = new User(username, password, birthday);
        //userController.addData(newUser);
        //userFileHandler.save(userController.getData());
    }
    
    private void validateUserData(String username, String password, Date birthday) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Usuario no puede estar vacío");
        }
        //if (userController.existsById(username.hashCode())) {
            //throw new Exception("El nombre de usuario ya está en uso");
        //}
        if (password == null || password.length() < 8) {
            throw new Exception("Contraseña debe ser mayor a 8 caracteres");
        }
        if (birthday == null) {
            throw new Exception("Fecha de nacimiento es requerida");
        }
    }
}
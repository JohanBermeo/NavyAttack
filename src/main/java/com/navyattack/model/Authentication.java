package com.navyattack.model;

public class Authentication {
    
    public static boolean login(String username, String password, User user) {
        if (user == null || password.isEmpty()) {
            return false;
        }
        if (!user.getPassword().equals(password)) {
            	return false;
        }
        return true;
    }
    
    public static User createAccount(String username, String password, String passwordConfirm, User existingUser) throws Exception {
        if (existingUser instanceof User) {
            throw new Exception("The user already exists");
        }

	    validateUserData(username, password, passwordConfirm);
        
        User newUser = new User(username, password);
    	return newUser;
    }
    
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
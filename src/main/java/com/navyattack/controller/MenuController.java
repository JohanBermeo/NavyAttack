package com.navyattack.controller;

import javafx.application.Application;
import javafx.stage.Stage;

import com.navyattack.model.User;
import com.navyattack.view.LoginView;
import com.navyattack.view.SignUpView;
import com.navyattack.model.DataManager;
import com.navyattack.model.Authentication;

public class MenuController {
    
	private LoginView loginView;
	private SignUpView signUpView;
	private DataManager dataManager;
    	private Authentication authModel;
    
    	public MenuController() {
		this.dataManager = new DataManager();
		this.authModel = new Authentication();
    	}
    
    	// Método para inicializar la vista y pasarle la referencia del controlador
    	public void initializeView(Stage primaryStage) {
        	this.loginView = new LoginView(this);
        	loginView.start(primaryStage);
    	}
    
    	// Método estático para lanzar la aplicación
    	public static void launchApp(String[] args) {
        	Application.launch(MenuApplication.class, args);
    	}
    
    	public boolean handleLogin(String username, String password) { 	
		User user = dataManager.findUser(username);
        	boolean result = authModel.login(username, password, user);
           	if (result) {
            		navigateToGameMenu();
            		return true;
        	} else {
			return false;
		}
    	}
    
    	// Método para manejar el registro
    	public boolean handleSignUp(String username, String password) {
        	// Lógica para registrar nuevo usuario
        	//boolean registered = authModel.registerUser(username, password);
        
        	//if (registered) {
            		//System.out.println("Usuario registrado exitosamente: " + username);
            	//return true;
        	//} else {
            	//System.out.println("Error al registrar usuario");
            	//return false;
        	//}
		return true;
    	}
    
    	// Método para navegar al menú del juego
    	private void navigateToGameMenu() {
        	System.out.println("Navegando al menú del juego...");
    	}

	public void navigateToSignUp(){
		Stage currentStage = (Stage) loginView.getScene().getWindow();
    		this.signUpView = new SignUpView(this);
    		signUpView.start(currentStage);
	}

	public void navigateToLogin(){
		Stage currentStage = (Stage) signUpView.getScene().getWindow();
    		loginView.start(currentStage);
	}
    
    	// Clase interna para manejar el lanzamiento de la aplicación JavaFX
    	public static class MenuApplication extends Application {
        	@Override
        	public void start(Stage primaryStage) {
            		MenuController controller = new MenuController();
            		controller.initializeView(primaryStage);
        	}
    	}
}
package com.navyattack.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.navyattack.controller.MenuController;

public class LoginView {

	private Scene scene;
   private Text messageLabel;
	private TextField usuarioField;
	private MenuController controller;
   private PasswordField passwordField;

	// Constructor que recibe la referencia del controlador
	public LoginView(MenuController controller) {
		this.controller = controller;
	}
	
	public void start(Stage stage) {

		// Crear el contenedor principal
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		// Título de la aplicación
		Text sceneTitle = new Text("Navy Attack");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
		GridPane.setHalignment(sceneTitle, javafx.geometry.HPos.CENTER);
		grid.add(sceneTitle, 0, 0, 1, 1);
		
		VBox whiteContainer = new VBox(12);
		whiteContainer.setStyle(
			"-fx-pref-width: 250px;" +
			"-fx-background-color: white;" +
			"-fx-background-radius: 10;" +    
			"-fx-border-color: #cccccc;" +   
			"-fx-border-width: 1;" +           
			"-fx-border-radius: 10;" +         
			"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
		);
		whiteContainer.setPadding(new Insets(20));
		whiteContainer.setAlignment(Pos.CENTER);
		whiteContainer.setMaxWidth(350);
		
		// Crear elementos de la interfaz
		Text labelU = new Text("Username");
		HBox labelUser = new HBox();
		labelUser.setAlignment(Pos.CENTER_LEFT);
		labelUser.getChildren().add(labelU);
		
		usuarioField = new TextField();
		
		Text labelP = new Text("Password");
		HBox labelPassword = new HBox();
		labelPassword.setAlignment(Pos.CENTER_LEFT);
		labelPassword.getChildren().add(labelP);
		
		passwordField = new PasswordField();
		
		// Botón de Login con evento
		Button btnLogin = new Button("Login");
		UtilsMenuView.styleButton(btnLogin);
		btnLogin.setOnAction(e -> handleLoginAction());

		// Texto para crear cuenta
		Text createAccountText = new Text("Create account");
		createAccountText.setOnMouseClicked(e -> handleSignUp());
		UtilsMenuView.styleText(createAccountText);
		
		// Label para mensajes
		messageLabel = new Text("");
		messageLabel.setStyle("-fx-fill: red;");
		
		whiteContainer.getChildren().addAll(
			labelUser, usuarioField, 
			labelPassword, passwordField, 
			btnLogin, createAccountText, messageLabel
		);
		
		grid.add(whiteContainer, 0, 1, 1, 10);
		
		// Crear y mostrar la escena
		Scene scene = new Scene(grid, 500, 450);
		scene.getRoot().setStyle("-fx-background-color: #5872C9;");
		this.scene = scene;
		stage.setTitle("Navy Attack - Login");
		stage.setScene(scene);
		stage.show();
	}
	
	// Método para manejar el evento de login
	private void handleLoginAction() {
		String username = usuarioField.getText();
		String password = passwordField.getText();
		
		boolean success = controller.handleLogin(username, password);
		
		if (success) {
			UtilsMenuView.showMessage("Login successful", "success", messageLabel);
		} else {
			UtilsMenuView.showMessage("Incorrect credentials", "error", messageLabel);
			passwordField.clear();
		}
	}
	
	// Método para manejar el evento de sign up
	private void handleSignUp() {
		controller.navigateToSignUp();
	}
    
	public Scene getScene() {
    	return scene;
	}
}
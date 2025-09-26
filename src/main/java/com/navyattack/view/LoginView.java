package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;

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
		if (controller.hasSingleUser()) {
			createSecondUserInterface(stage);
		} else {
			createMainInterface(stage);
		}
	}
	
	private void createMainInterface(Stage stage) {
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
		
		VBox whiteContainer = createLoginContainer();
		
		grid.add(whiteContainer, 0, 1, 1, 10);
		
		// Crear y mostrar la escena
		Scene scene = new Scene(grid, 500, 450);
		scene.getRoot().setStyle("-fx-background-color: #5872C9;");
		this.scene = scene;
		stage.setTitle("Navy Attack - Login");
		stage.setScene(scene);
		stage.show();
	}
	
	private void createSecondUserInterface(Stage stage) {

		// Crear el contenedor principal
		BorderPane root = new BorderPane();

		// TopPanel fijo en la parte superior
		VBox topPanel = new VBox(); 
		topPanel.setAlignment(Pos.CENTER_LEFT); 
		topPanel.setPadding(new Insets(15, 15, 15, 15)); 
				
		ImageView backArrow = UtilsMenuView.createImage("file:docs/Icons/png/flecha-pequena-izquierda.png"); 
		backArrow.setStyle("-fx-cursor: hand;");                   
		backArrow.setOnMouseClicked(e -> controller.navigateToGameMenu());

		topPanel.getChildren().add(backArrow); 

		// Grid para el contenido central
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 0, 25));

		// Título de la aplicación
		Text sceneTitle = new Text("Navy Attack");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
		GridPane.setHalignment(sceneTitle, javafx.geometry.HPos.CENTER);
		grid.add(sceneTitle, 0, 0, 1, 1);

		VBox whiteContainer = createLoginContainer();

		grid.add(whiteContainer, 0, 1, 1, 1);

		// Establecer las regiones del BorderPane
		root.setTop(topPanel);
		root.setCenter(grid);
		root.setStyle("-fx-background-color: #C95858;");

		// Crear y mostrar la escena
		Scene scene = new Scene(root, 500, 500);
		this.scene = scene;
		stage.setTitle("Navy Attack");
		stage.setScene(scene);
		stage.show();
	}
	
	private VBox createLoginContainer() {
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
		UtilsMenuView.styleButton(btnLogin, "black", "#333333", "white", "5px 0 5px 0");
		btnLogin.setMaxWidth(Double.MAX_VALUE);
		btnLogin.setOnAction(e -> handleLoginAction());

		// Texto para crear cuenta (solo en interfaz principal)
		Text createAccountText = new Text("Create account");
		createAccountText.setOnMouseClicked(e -> controller.navigateToSignUp());
		UtilsMenuView.styleText(createAccountText);
		
		// Label para mensajes
		messageLabel = new Text("");
		messageLabel.setStyle("-fx-fill: red;");
		
		whiteContainer.getChildren().addAll(
			labelUser, usuarioField, 
			labelPassword, passwordField, 
			btnLogin, createAccountText, messageLabel
		);
		
		return whiteContainer;
	}
	
	// Método para manejar el evento de login
	private void handleLoginAction() {
		String username = usuarioField.getText();
		String password = passwordField.getText();
		
		boolean success = controller.handleLogin(username, password);
		
		if (!success) {
			UtilsMenuView.showMessage("Incorrect credentials", "error", messageLabel);
			passwordField.clear();
		} 
	}
	
	public Scene getScene() {
		return scene;
	}
}
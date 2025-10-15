package com.navyattack.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import com.navyattack.controller.MenuController;
import com.navyattack.controller.NavigationController;

/**
 * Clase que representa la vista de registro (Sign Up) en el sistema NavyAttack.
 * Permite a los usuarios crear una nueva cuenta ingresando un nombre de usuario 
 * y una contraseña, gestionando además la validación de credenciales y navegación 
 * entre vistas.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class SignUpView implements IView {

	/** Escena principal de la vista. */
	private Scene scene;
	/** Etiqueta de texto para mostrar mensajes de error o éxito. */
	private Text messageLabel;
	/** Campo de texto para ingresar el nombre de usuario. */
	private TextField usuarioField;
	/** Campo de texto para ingresar la contraseña. */
	private PasswordField passwordField;
	/** Controlador encargado de la lógica de registro y validación. */
	private MenuController menuController;
	/** Campo para confirmar la contraseña ingresada. */
	private PasswordField confirmPasswordField;
	/** Controlador encargado de la navegación entre vistas. */
	private NavigationController navigationController;
	
	/**
	 * Constructor de la vista de registro.
	 *
	 * @param menuController controlador del menú principal.
	 * @param controller controlador de navegación entre vistas.
	 */
	public SignUpView(MenuController menuController, NavigationController controller) {
		this.menuController = menuController;
		this.navigationController = controller;
	}
	
	/**
	 * Inicia la vista de registro. 
	 * Muestra una interfaz diferente dependiendo del número de usuarios registrados.
	 *
	 * @param stage el {@link Stage} principal donde se mostrará la vista.
	 */
	@Override
	public void start(Stage stage) {
		if (menuController.hasSingleUser()) {
			createSecondUserInterface(stage);
		} else {
			createMainInterface(stage);
		}
	}
	
	/**
	 * Crea la interfaz principal de registro para el primer usuario.
	 *
	 * @param stage el escenario donde se mostrará la interfaz.
	 */
	private void createMainInterface(Stage stage) {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text sceneTitle = new Text("Navy Attack");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
		GridPane.setHalignment(sceneTitle, javafx.geometry.HPos.CENTER);
		grid.add(sceneTitle, 0, 0, 1, 1);
		
		VBox whiteContainer = createSignUpContainer();
		grid.add(whiteContainer, 0, 1, 1, 10);
		
		Scene scene = new Scene(grid, 500, 450);
		scene.getRoot().setStyle("-fx-background-color: #5872C9;");
		this.scene = scene;
		stage.setTitle("Navy Attack - Sign Up");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Crea la interfaz de registro para el segundo usuario.
	 *
	 * @param stage el escenario donde se mostrará la interfaz.
	 */
	private void createSecondUserInterface(Stage stage) {
		BorderPane root = new BorderPane();

		VBox topPanel = new VBox(); 
		topPanel.setAlignment(Pos.CENTER_LEFT); 
		topPanel.setPadding(new Insets(15, 15, 0, 15)); 
				
		ImageView backArrow = UtilsMenuView.createImage("file:docs/Icons/png/flecha-pequena-izquierda.png"); 
		backArrow.setStyle("-fx-cursor: hand;");                   
		backArrow.setOnMouseClicked(e -> navigationController.navigateToView("menu"));

		topPanel.getChildren().add(backArrow); 

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 25, 0, 25));

		Text sceneTitle = new Text("Navy Attack");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
		GridPane.setHalignment(sceneTitle, javafx.geometry.HPos.CENTER);
		grid.add(sceneTitle, 0, 0, 1, 1);

		VBox whiteContainer = createSignUpContainer();
		grid.add(whiteContainer, 0, 1, 1, 1);

		root.setTop(topPanel);
		root.setCenter(grid);
		root.setStyle("-fx-background-color: #C95858;");

		Scene scene = new Scene(root, 500, 500);
		this.scene = scene;
		stage.setTitle("Navy Attack");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Crea el contenedor blanco con los campos de texto y botones 
	 * necesarios para el registro de usuario.
	 *
	 * @return un {@link VBox} con los elementos del formulario de registro.
	 */
	private VBox createSignUpContainer() {
		VBox whiteContainer = new VBox(12);
		whiteContainer.setStyle(
			"-fx-pref-width: 300px;" +
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
		
		Text labelCP = new Text("Confirm password");
		HBox labelConfirmPassword = new HBox();
		labelConfirmPassword.setAlignment(Pos.CENTER_LEFT);
		labelConfirmPassword.getChildren().add(labelCP);
		
		confirmPasswordField = new PasswordField();
		
		Button btnSignUp = new Button("Sign up");
		btnSignUp.setMaxWidth(Double.MAX_VALUE);
		UtilsMenuView.styleButton(btnSignUp, "black", "#333333", "white", "5px 0 5px 0");
		btnSignUp.setOnAction(e -> handleSignUpAction());

		messageLabel = new Text("");
		messageLabel.setStyle("-fx-fill: red;");
		
		Text signInText = new Text("Sign in");
		signInText.setOnMouseClicked(e -> handleSignIn());
		UtilsMenuView.styleText(signInText);
		
		whiteContainer.getChildren().addAll(
			labelUser, usuarioField, 
			labelPassword, passwordField,
			labelConfirmPassword, confirmPasswordField,
			btnSignUp, signInText, messageLabel
		);
		
		return whiteContainer;
	}
	
	/**
	 * Maneja el evento de registro (Sign Up).
	 * Valida los datos ingresados, muestra mensajes de error o éxito 
	 * y redirige al menú principal si el registro fue exitoso.
	 */
	private void handleSignUpAction() {
		String username = usuarioField.getText();
		String password = passwordField.getText();
		String passwordConfirm = confirmPasswordField.getText();
		
		boolean success = false;
		try {
			success = menuController.handleSignUp(username, password, passwordConfirm);
		} catch (Exception e) {
			UtilsMenuView.showMessage(e.getMessage(), "error", messageLabel);
		}
		if (success) {
			UtilsMenuView.showMessage("Account created", "success", messageLabel);
			navigationController.navigateToView("menu");
		}
	}
	
	/**
	 * Maneja el evento para cambiar a la vista de inicio de sesión (Sign In).
	 */
	private void handleSignIn() {
		navigationController.navigateToView("login");
	}

	/**
	 * Devuelve la escena asociada a la vista actual.
	 *
	 * @return la {@link Scene} de esta vista.
	 */
	@Override
	public Scene getScene() {
		return scene;
	}
}

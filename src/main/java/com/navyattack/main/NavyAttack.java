package main;

import javafx.application.Application;
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

public class NavyAttack extends Application {
    
    @Override
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
    		"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);" // Sombra sutil
	);
	whiteContainer.setPadding(new Insets(20));
	whiteContainer.setAlignment(Pos.CENTER);
	whiteContainer.setMaxWidth(350);

	// Agregar elementos
	Text labelU = new Text("Username");
	HBox labelUser = new HBox();
	labelUser.setAlignment(Pos.CENTER_LEFT);
	labelUser.getChildren().add(labelU);
	TextField usuario = new TextField();

	Text labelP = new Text("Password");
	HBox labelPassword = new HBox();
	labelPassword.setAlignment(Pos.CENTER_LEFT);
	labelPassword.getChildren().add(labelP);
	PasswordField password = new PasswordField();


	Button btnLogin = new Button("Login");
	btnLogin.setStyle(
    		"-fx-background-color: black;" +
    		"-fx-text-fill: white;" +
    		"-fx-font-size: 12px;" +
    		"-fx-font-weight: bold;" +
    		"-fx-background-radius: 5;"      
	);
	btnLogin.setMaxWidth(Double.MAX_VALUE);
	btnLogin.setOnMouseEntered(e -> 
    		btnLogin.setStyle(
        		"-fx-background-color: #333333;" + 
        		"-fx-text-fill: white;" +
        		"-fx-font-size: 12px;" +
			"-fx-font-weight: bold;"
    		)
	);
	btnLogin.setOnMouseExited(e -> 
    		btnLogin.setStyle(
        		"-fx-background-color: black;" +
        		"-fx-text-fill: white;" +
        		"-fx-font-size: 12px;" +
			"-fx-font-weight: bold;" +
			"-fx-background-radius: 5;"  
   	 	)
	);

	Button btnSignUp = new Button("Sign up");
	btnSignUp.setStyle(
    		"-fx-background-color: black;" +
    		"-fx-text-fill: white;" +
    		"-fx-font-size: 12px;" +
    		"-fx-font-weight: bold;" +
    		"-fx-background-radius: 5;"       
	);
	btnSignUp.setMaxWidth(Double.MAX_VALUE);

	Text labelMessage = new Text("");

	whiteContainer.getChildren().addAll(labelUser, usuario, labelPassword, password, btnLogin, btnSignUp, labelMessage);
        grid.add(whiteContainer, 0, 1, 1, 10);
        
        // Crear y mostrar la escena
        Scene scene = new Scene(grid, 500, 450);
        scene.getRoot().setStyle("-fx-background-color: #5872C9;");
        stage.setTitle("Navy Attack - Login");
        stage.setScene(scene);
        stage.show();
    }
    
    // Método simple de autenticación (para demostración)
    private boolean autenticar(String usuario, String password) {
        // Credenciales de ejemplo - en una aplicación real, esto vendría de una base de datos
        return (usuario.equals("admin") && password.equals("admin")) ||
               (usuario.equals("player1") && password.equals("123456"));
    }
    
    public static void main(String[] args) {
        launch();
    }
}

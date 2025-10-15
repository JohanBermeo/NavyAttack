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
import com.navyattack.controller.NavigationController;

/**
 * Vista encargada del inicio de sesión de los jugadores.
 * Permite autenticar usuarios existentes y redirigir a la vista de registro o menú principal.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class LoginView implements IView {

    /** Escena principal de la vista de inicio de sesión. */
    private Scene scene;

    /** Etiqueta para mostrar mensajes de error o información. */
    private Text messageLabel;

    /** Campo de texto para ingresar el nombre de usuario. */
    private TextField usuarioField;

    /** Controlador del menú principal que gestiona la autenticación. */
    private MenuController menuController;

    /** Controlador de navegación para cambiar entre vistas. */
    private NavigationController navigationController;

    /** Campo de texto para ingresar la contraseña. */
    private PasswordField passwordField;

    /**
     * Constructor de la clase LoginView.
     *
     * @param controller controlador del menú principal.
     * @param navigationController controlador encargado de la navegación entre vistas.
     */
    public LoginView(MenuController controller, NavigationController navigationController) {
        this.menuController = controller;
        this.navigationController = navigationController;
    }

    /**
     * Inicia la vista según la cantidad de usuarios registrados.
     * Si existe un solo usuario, se muestra la interfaz para el segundo jugador.
     *
     * @param stage el escenario principal donde se mostrará la vista.
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
     * Crea la interfaz principal del login (primer usuario).
     *
     * @param stage el escenario principal.
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

        VBox whiteContainer = createLoginContainer();
        grid.add(whiteContainer, 0, 1, 1, 10);

        Scene scene = new Scene(grid, 500, 450);
        scene.getRoot().setStyle("-fx-background-color: #5872C9;");
        this.scene = scene;
        stage.setTitle("Navy Attack - Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crea la interfaz de inicio de sesión para el segundo jugador.
     *
     * @param stage el escenario principal.
     */
    private void createSecondUserInterface(Stage stage) {
        BorderPane root = new BorderPane();

        VBox topPanel = new VBox();
        topPanel.setAlignment(Pos.CENTER_LEFT);
        topPanel.setPadding(new Insets(15, 15, 15, 15));

        ImageView backArrow = UtilsMenuView.createImage("file:docs/Icons/png/flecha-pequena-izquierda.png");
        backArrow.setStyle("-fx-cursor: hand;");
        backArrow.setOnMouseClicked(e -> navigationController.navigateToView("menu"));

        topPanel.getChildren().add(backArrow);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 0, 25));

        Text sceneTitle = new Text("Navy Attack");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
        GridPane.setHalignment(sceneTitle, javafx.geometry.HPos.CENTER);
        grid.add(sceneTitle, 0, 0, 1, 1);

        VBox whiteContainer = createLoginContainer();
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
     * Crea el contenedor con los campos de texto, botones y etiquetas
     * para el inicio de sesión.
     *
     * @return un {@link VBox} con los componentes de la interfaz.
     */
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

        Button btnLogin = new Button("Login");
        UtilsMenuView.styleButton(btnLogin, "black", "#333333", "white", "5px 0 5px 0");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setOnAction(e -> handleLoginAction());

        Text createAccountText = new Text("Create account");
        createAccountText.setOnMouseClicked(e -> navigationController.navigateToView("signup"));
        UtilsMenuView.styleText(createAccountText);

        messageLabel = new Text("");
        messageLabel.setStyle("-fx-fill: red;");

        whiteContainer.getChildren().addAll(
            labelUser, usuarioField,
            labelPassword, passwordField,
            btnLogin, createAccountText, messageLabel
        );

        return whiteContainer;
    }

    /**
     * Maneja la acción de inicio de sesión.
     * Valida las credenciales ingresadas y redirige según el resultado.
     */
    private void handleLoginAction() {
        String username = usuarioField.getText();
        String password = passwordField.getText();

        boolean success = menuController.handleLogin(username, password);

        if (success) {
            navigationController.navigateToView("menu");
        } else {
            UtilsMenuView.showMessage("Incorrect credentials", "error", messageLabel);
            passwordField.clear();
        }
    }

    /**
     * Obtiene la escena asociada a la vista actual.
     *
     * @return la {@link Scene} del inicio de sesión.
     */
    @Override
    public Scene getScene() {
        return scene;
    }
}

package com.navyattack.view; 

import java.util.List; 

import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.geometry.Pos; 
import javafx.geometry.HPos; 
import javafx.scene.text.Text; 
import javafx.scene.text.Font; 
import javafx.geometry.Insets; 
import javafx.scene.layout.HBox; 
import javafx.scene.layout.Region; 
import javafx.scene.control.Button; 
import javafx.scene.image.ImageView; 
import javafx.scene.layout.GridPane; 
import javafx.scene.text.FontWeight; 
import javafx.scene.layout.BorderPane; 
import javafx.application.Application; 

import com.navyattack.model.History; 
import com.navyattack.controller.MenuController;
import com.navyattack.controller.NavigationController;

/**
 * Vista principal del menú del juego NavyAttack.
 * Gestiona la interfaz de usuario para uno o dos jugadores, 
 * mostrando opciones como jugar, ver historial o cerrar sesión.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class MenuView extends Application implements IView { 

    /** Escena principal del menú. */
    private Scene scene; 

    /** Controlador encargado de la lógica del menú y usuarios. */
    private MenuController menuController;

    /** Controlador encargado de la navegación entre vistas. */
    private NavigationController navigationController; 

    /**
     * Constructor de la clase MenuView.
     *
     * @param menuController controlador del menú principal.
     * @param controller controlador de navegación entre vistas.
     */
    public MenuView(MenuController menuController, NavigationController controller) { 
        this.menuController = menuController;
        this.navigationController = controller; 
    } 

    /**
     * Inicia la vista del menú.
     * Muestra una interfaz distinta dependiendo de si hay uno o dos usuarios conectados.
     *
     * @param stage escenario principal donde se muestra la vista.
     */
    @Override 
    public void start(Stage stage) { 
        if (menuController.hasSingleUser()) {
            createSingleUserInterface(stage);
        } else {
            createMultiUserInterface(stage);
        }
    }
    
    /**
     * Crea la interfaz del menú cuando solo hay un usuario conectado.
     *
     * @param stage escenario principal.
     */
    private void createSingleUserInterface(Stage stage) {
        BorderPane mainPane = new BorderPane();          

        ImageView imageUser = UtilsMenuView.createImage("file:src/main/resources/images/icons/user.png");  

        Text leftText = new Text("Usuario: " + menuController.getLoggedUsers().get(0).getUsername()); 
        leftText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));  
        leftText.setStyle("-fx-fill: white;");
        leftText.setOnMouseEntered(e -> leftText.setStyle("-fx-fill: #EDEDED;"));
        leftText.setOnMouseExited(e -> leftText.setStyle("-fx-fill: white;"));

        HBox leftContainer = new HBox(5); 
        leftContainer.setAlignment(Pos.CENTER_LEFT); 
        leftContainer.getChildren().addAll(imageUser, leftText); 

        ImageView imageAdd = UtilsMenuView.createImage("file:src/main/resources/images/icons/001-botn-agregar.png");
        imageAdd.setStyle("-fx-cursor: hand;");
        imageAdd.setOnMouseClicked(event -> navigationController.navigateToView("login"));

        Text rightText = new Text("Add new user"); 
        rightText.setStyle("-fx-fill: white;");
        rightText.setOnMouseEntered(e -> rightText.setStyle(
            "-fx-underline: true;" +
            "-fx-fill: #EDEDED;" +
            "-fx-cursor: hand;"
        ));
        rightText.setOnMouseExited(e -> rightText.setStyle(
            "-fx-underline: false;" +
            "-fx-fill: white;"
        ));
        rightText.setOnMouseClicked(event -> navigationController.navigateToView("login"));
        rightText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20)); 

        HBox rightContainer = new HBox(5); 
        rightContainer.setAlignment(Pos.CENTER_LEFT); 
        rightContainer.getChildren().addAll(imageAdd, rightText);  

        BorderPane topPane = new BorderPane(); 
        topPane.setLeft(leftContainer); 
        topPane.setRight(rightContainer); 
        topPane.setPadding(new Insets(20, 30, 0, 30)); 
        mainPane.setTop(topPane); 

        GridPane grid = new GridPane(); 
        grid.setAlignment(Pos.CENTER); 
        grid.setHgap(10); 
        grid.setVgap(20); 
        grid.setPadding(new Insets(25, 25, 25, 25)); 

        Text sceneTitle = new Text("NavyAttack"); 
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 50)); 
        GridPane.setHalignment(sceneTitle, HPos.CENTER); 
        grid.add(sceneTitle, 0, 0, 1, 1); 

        Region spacer = new Region(); 
        spacer.setPrefHeight(40); 
        grid.add(spacer, 0, 1, 1, 1); 

        Button playBtn = new Button("Play"); 
        playBtn.setMaxWidth(125); 
        UtilsMenuView.styleButton(playBtn, "black", "#333333", "white", "10px 0 10px 0"); 
        playBtn.setOnAction(e -> navigationController.navigateToView("play"));
        GridPane.setHalignment(playBtn, HPos.CENTER); 
        grid.add(playBtn, 0, 2, 1, 1); 

        Button historyBtn = new Button("History"); 
        historyBtn.setMaxWidth(125); 
        UtilsMenuView.styleButton(historyBtn, "black", "#333333", "white", "10px 0 10px 0"); 
        historyBtn.setOnAction(e -> navigationController.navigateToHistory(
            menuController.getLoggedUsers().get(0).getUsername(), 
            menuController.getLoggedUsers().get(0).getHistory()
        )); 
        GridPane.setHalignment(historyBtn, HPos.CENTER); 	
        grid.add(historyBtn, 0, 3, 1, 1); 

        Button logoutBtn = new Button("Log out"); 
        logoutBtn.setMaxWidth(125); 
        UtilsMenuView.styleButton(logoutBtn, "white", "#EDEDED", "black", "10px 0 10px 0"); 
        logoutBtn.setOnAction(e -> { 
            navigationController.logoutUser(menuController.getLoggedUsers().get(0).getUsername());
            navigationController.navigateToView("login");
        }); 
        GridPane.setHalignment(logoutBtn, HPos.CENTER); 
        grid.add(logoutBtn, 0, 4, 1, 1); 
        mainPane.setCenter(grid); 

        Scene scene = new Scene(mainPane, 1080, 720); 
        scene.getRoot().setStyle("-fx-background-color: #5872C9;"); 
        this.scene = scene; 
        stage.setTitle("Navy Attack"); 
        stage.setScene(scene); 
        stage.show(); 
    }
    
    /**
     * Crea la interfaz del menú cuando hay dos usuarios conectados.
     *
     * @param stage escenario principal.
     */
    private void createMultiUserInterface(Stage stage) {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: linear-gradient(to right, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);");
        
        HBox leftUserContainer = createUserContainer(
            menuController.getLoggedUsers().get(0).getUsername(),
            menuController.getLoggedUsers().get(0).getHistory()
        );
        
        HBox rightUserContainer = createUserContainer(
            menuController.getLoggedUsers().get(1).getUsername(),
            menuController.getLoggedUsers().get(1).getHistory()
        );

        BorderPane topPane = new BorderPane(); 
        topPane.setLeft(leftUserContainer); 
        topPane.setRight(rightUserContainer); 
        topPane.setPadding(new Insets(20, 30, 0, 30)); 
        mainPane.setTop(topPane); 

        GridPane grid = new GridPane(); 
        grid.setAlignment(Pos.CENTER); 
        grid.setHgap(10); 
        grid.setVgap(20); 
        grid.setPadding(new Insets(0, 25, 25, 25)); 

        Text sceneTitle = new Text("NavyAttack"); 
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 50)); 
        sceneTitle.setFill(javafx.scene.paint.Color.WHITE);
        GridPane.setHalignment(sceneTitle, HPos.CENTER); 
        grid.add(sceneTitle, 0, 0, 1, 1); 

        Region spacer = new Region(); 
        spacer.setPrefHeight(10); 
        grid.add(spacer, 0, 1, 1, 1); 

        Button playBtn = new Button("PLAY"); 
        playBtn.setMaxWidth(125); 
        playBtn.setOnAction(e -> navigationController.navigateToView("play"));
        UtilsMenuView.styleButton(playBtn, "black", "#333333", "white", "10px 0 10px 0"); 
        GridPane.setHalignment(playBtn, HPos.CENTER); 
        grid.add(playBtn, 0, 2, 1, 1); 
        mainPane.setCenter(grid); 

        Scene scene = new Scene(mainPane, 1080, 720); 
        this.scene = scene; 
        stage.setTitle("Navy Attack"); 
        stage.setScene(scene); 
        stage.show(); 
    }
    
    /**
     * Crea un contenedor visual con la información de un usuario,
     * incluyendo su nombre, historial y botones de acción.
     *
     * @param username nombre del usuario.
     * @param history lista del historial de partidas del usuario.
     * @return un {@link HBox} con los elementos visuales del usuario.
     */
    private HBox createUserContainer(String username, List<History> history) {
        HBox userContainer = new HBox(10);
        userContainer.setAlignment(Pos.CENTER_LEFT);
        userContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;"
        );
        
        ImageView userIcon = UtilsMenuView.createImage("file:src/main/resources/images/icons/user.png");
        
        Text usernames = new Text(username);
        usernames.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        
        Button historyBtn = new Button("History");
        UtilsMenuView.styleButton(historyBtn, "black", "#333333", "white", "5px 10px 5px 10px");
        historyBtn.setOnAction(e -> navigationController.navigateToHistory(username, history));
        
        Button logoutBtn = new Button("Logout");
        UtilsMenuView.styleButton(logoutBtn, "white", "#EDEDED", "black", "5px 10px 5px 10px");
        logoutBtn.setOnAction(e -> navigationController.logoutUser(username));
        
        userContainer.getChildren().addAll(userIcon, usernames, historyBtn, logoutBtn);
        return userContainer;
    }

    /**
     * Obtiene la escena asociada a la vista del menú.
     *
     * @return la {@link Scene} correspondiente a la vista actual.
     */
    @Override
    public Scene getScene() { 
        return scene; 
    } 
}

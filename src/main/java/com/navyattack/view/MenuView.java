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

import com.navyattack.model.User; 
import com.navyattack.controller.MenuController; 
 
public class MenuView extends Application { 

    private Scene scene; 
    private List<User> loggedUsers; 
    private MenuController controller; 

    // Constructor que recibe la referencia del controlador 
    public MenuView(MenuController controller, List<User> loggedUsers) { 
        this.controller = controller; 
        this.loggedUsers = loggedUsers; 
    } 

    @Override 
    public void start(Stage stage) { 
        if (controller.hasSingleUser()) {
            createSingleUserInterface(stage);
        } else {
            createMultiUserInterface(stage);
        }
    }
    
    private void createSingleUserInterface(Stage stage) {
        BorderPane mainPane = new BorderPane();          

        ImageView imageUser = UtilsMenuView.createImage("file:docs/Icons/png/user.png");  

        Text leftText = new Text("Usuario: " + this.loggedUsers.get(0).getUsername()); 
        leftText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));  
	    leftText.setStyle("-fx-fill: white;");
	    leftText.setOnMouseEntered(e -> 
            leftText.setStyle("-fx-fill: #EDEDED;")
	    );
        leftText.setOnMouseExited(e -> 
            leftText.setStyle("-fx-fill: white;")
        );

        HBox leftContainer = new HBox(5); 
        leftContainer.setAlignment(Pos.CENTER_LEFT); 
        leftContainer.getChildren().addAll(imageUser, leftText); 

        ImageView imageAdd = UtilsMenuView.createImage("file:docs/Icons/png/001-botn-agregar.png"); 

        Text rightText = new Text("Add new user"); 
	    rightText.setStyle("-fx-fill: white;");
        rightText.setOnMouseEntered(e -> 
            rightText.setStyle(
                "-fx-underline: true;" +
		        "-fx-fill: #EDEDED;" +
                "-fx-cursor: hand;"
            )
        );
        rightText.setOnMouseExited(e -> 
            rightText.setStyle(
                "-fx-underline: false;" + 
		        "-fx-fill: white;"
            )
        );
        rightText.setOnMouseClicked(event -> {
            controller.navigateToSecondUserLogin();
        });
        rightText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20)); 
        rightText.setFill(javafx.scene.paint.Color.BLACK); 

        HBox rightContainer = new HBox(5); 
        rightContainer.setAlignment(Pos.CENTER_LEFT); 
        rightContainer.getChildren().addAll(imageAdd, rightText);  

        // Posicionar textos en las esquinas superiores 
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
        playBtn.setOnAction(e -> { 
            controller.navigateToPlay();
        }); 
	    GridPane.setHalignment(playBtn, HPos.CENTER); 
        grid.add(playBtn, 0, 2, 1, 1); 

        Button historyBtn = new Button("History"); 
        historyBtn.setMaxWidth(125); 
        UtilsMenuView.styleButton(historyBtn, "black", "#333333", "white", "10px 0 10px 0"); 
        historyBtn.setOnAction(e -> { 
            controller.navigateToHistory(this.loggedUsers.get(0));
        }); 
        GridPane.setHalignment(historyBtn, HPos.CENTER); 	
        grid.add(historyBtn, 0, 3, 1, 1); 

        Button logoutBtn = new Button("Log out"); 
        logoutBtn.setMaxWidth(125); 
        UtilsMenuView.styleButton(logoutBtn, "white", "#EDEDED", "black", "10px 0 10px 0"); 
        logoutBtn.setOnAction(e -> { 
            controller.logoutUser(loggedUsers.get(0));
            controller.navigateToLogin();
        }); 
        GridPane.setHalignment(logoutBtn, HPos.CENTER); 
        grid.add(logoutBtn, 0, 4, 1, 1); 
        mainPane.setCenter(grid); 

        // Crear y mostrar la escena 
        Scene scene = new Scene(mainPane, 1080, 720); 
        scene.getRoot().setStyle("-fx-background-color: #5872C9;"); 
        this.scene = scene; 
        stage.setTitle("Navy Attack"); 
        stage.setScene(scene); 
        stage.show(); 
    }
    
    private void createMultiUserInterface(Stage stage) {
        BorderPane mainPane = new BorderPane();
        
        // Crear fondo dividido con dos colores usando CSS linear gradient
        mainPane.setStyle("-fx-background-color: linear-gradient(to right, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);");
        
        // Crear userContainer para el primer usuario
        HBox leftUserContainer = createUserContainer(loggedUsers.get(0));
        
        // Crear userContainer para el segundo usuario
        HBox rightUserContainer = createUserContainer(loggedUsers.get(1));

        // Posicionar userContainers en las esquinas superiores 
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
	    playBtn.setOnAction(e -> { 
            controller.navigateToPlay();
        }); 
        UtilsMenuView.styleButton(playBtn, "black", "#333333", "white", "10px 0 10px 0"); 
        GridPane.setHalignment(playBtn, HPos.CENTER); 
        grid.add(playBtn, 0, 2, 1, 1); 
        mainPane.setCenter(grid); 

        // Crear y mostrar la escena 
        Scene scene = new Scene(mainPane, 1080, 720); 
        this.scene = scene; 
        stage.setTitle("Navy Attack"); 
        stage.setScene(scene); 
        stage.show(); 
    }
    
    private HBox createUserContainer(User user) {
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
        
        ImageView userIcon = UtilsMenuView.createImage("file:docs/Icons/png/user.png");
        
        Text username = new Text(user.getUsername());
        username.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        
        Button historyBtn = new Button("History");
        UtilsMenuView.styleButton(historyBtn, "black", "#333333", "white", "5px 10px 5px 10px");
        historyBtn.setOnAction(e -> controller.navigateToHistory(user));
        
        Button logoutBtn = new Button("Logout");
        UtilsMenuView.styleButton(logoutBtn, "white", "#EDEDED", "black", "5px 10px 5px 10px");
        logoutBtn.setOnAction(e -> controller.logoutUser(user));
        
        userContainer.getChildren().addAll(userIcon, username, historyBtn, logoutBtn);
        return userContainer;
    }

    public Scene getScene() { 
        return scene; 
    } 
} 
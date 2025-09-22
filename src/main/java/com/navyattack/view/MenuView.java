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

        BorderPane mainPane = new BorderPane();          

        ImageView imageUser = UtilsMenuView.createImage("file:docs/Icons/png/user.png");  

        Text leftText = new Text("Usuario: " + this.loggedUsers.get(0).getUsername()); 
        leftText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));  

        HBox leftContainer = new HBox(5); 
        leftContainer.setAlignment(Pos.CENTER_LEFT); 
        leftContainer.getChildren().addAll(imageUser, leftText); 

        ImageView imageAdd = UtilsMenuView.createImage("file:docs/Icons/png/001-botn-agregar.png"); 

        Text rightText = new Text("Add new user"); 
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
        GridPane.setHalignment(playBtn, HPos.CENTER); 
        grid.add(playBtn, 0, 2, 1, 1); 

        Button historyBtn = new Button("History"); 
        historyBtn.setMaxWidth(125); 
        UtilsMenuView.styleButton(historyBtn, "black", "#333333", "white", "10px 0 10px 0"); 
        GridPane.setHalignment(historyBtn, HPos.CENTER); 
        grid.add(historyBtn, 0, 3, 1, 1); 

        Button logoutBtn = new Button("Log out"); 
        logoutBtn.setMaxWidth(125); 
        UtilsMenuView.styleButton(logoutBtn, "white", "#EDEDED", "black", "10px 0 10px 0"); 
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

    public Scene getScene() { 
        return scene; 
    } 
} 
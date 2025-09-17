package com.navyattack.view;

import java.util.List;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.image.ImageView;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
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
        
        // Texto esquina superior izquierda
        Text topLeftText = new Text("Usuario: " + this.loggedUsers.get(0).getUsername());
        topLeftText.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        topLeftText.setFill(javafx.scene.paint.Color.BLACK);
        
        ImageView iconImage = new ImageView();
        try {
            Image image = new Image("file:docs/Icons/png/001-botn-agregar.png"); 
            iconImage.setImage(image);
            iconImage.setFitWidth(30); 
            iconImage.setFitHeight(30); 
            iconImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + e.getMessage());
        }
        // Texto esquina superior derecha
        Text topRightText = new Text("Add new user");
        topRightText.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        topRightText.setFill(javafx.scene.paint.Color.BLACK);
        
        HBox versionContainer = new HBox(5);
        versionContainer.setAlignment(Pos.CENTER_LEFT);
        versionContainer.getChildren().addAll(iconImage, topRightText);

        // Posicionar textos en las esquinas superiores
        BorderPane topPane = new BorderPane();
        topPane.setLeft(topLeftText);
        topPane.setRight(versionContainer);
        topPane.setPadding(new Insets(10, 15, 0, 15));
        
        mainPane.setTop(topPane);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Título de la aplicación
        Text sceneTitle = new Text("NavyAttack");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 40));
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
        grid.add(sceneTitle, 0, 0, 1, 1);

        // Agregar espacio entre título y botones
        Region spacer = new Region();
        spacer.setPrefHeight(30);
        grid.add(spacer, 0, 1, 1, 1);

        Button playBtn = new Button("Play");
        playBtn.setMaxWidth(100);
        playBtn.setStyle(
            "-fx-background-color: black;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10px 0 10px 0;"
        );
        
        playBtn.setOnMouseEntered(e -> 
            playBtn.setStyle(
                "-fx-background-color: #333333;" + 
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" + 
                "-fx-padding: 10px 0 10px 0;"
            )
        );
        
        playBtn.setOnMouseExited(e -> 
            playBtn.setStyle(
                "-fx-background-color: black;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 10px 0 10px 0;"
            )
        );
        GridPane.setHalignment(playBtn, HPos.CENTER);
        grid.add(playBtn, 0, 2, 1, 1);

        Button historyBtn = new Button("History");
        historyBtn.setMaxWidth(100);
        historyBtn.setStyle(
            "-fx-background-color: black;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10px 0 10px 0;"
        );
        
        historyBtn.setOnMouseEntered(e -> 
            historyBtn.setStyle(
                "-fx-background-color: #333333;" + 
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" + 
                "-fx-padding: 10px 0 10px 0;"
            )
        );
        
        historyBtn.setOnMouseExited(e -> 
            historyBtn.setStyle(
                "-fx-background-color: black;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 10px 0 10px 0;"
            )
        );
        GridPane.setHalignment(historyBtn, HPos.CENTER);
        grid.add(historyBtn, 0, 3, 1, 1);

        Button logoutBtn = new Button("Log out");
        logoutBtn.setStyle(
            "-fx-text-fill: black; -fx-background-color: white; " +
            "-fx-font-weight: bold;-fx-padding: 10px 0 10px 0;"
        );
        logoutBtn.setMaxWidth(100);
        logoutBtn.setOnMouseEntered(e ->
        logoutBtn.setStyle(
            "-fx-background-color: #EDEDED;" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10px 0 10px 0;"
        )
        );
        logoutBtn.setOnMouseExited(e ->
        logoutBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10px 0 10px 0;"
        )
        );
        GridPane.setHalignment(logoutBtn, HPos.CENTER);
        grid.add(logoutBtn, 0, 4, 1, 1);

        // Agregar el grid al centro del BorderPane
        mainPane.setCenter(grid);

        // Crear y mostrar la escena
        Scene scene = new Scene(mainPane, 800, 450);
        scene.getRoot().setStyle("-fx-background-color: #5872C9;");
        this.scene = scene;
        stage.setTitle("Navy Attack - Login");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
	    return scene;
    }
}
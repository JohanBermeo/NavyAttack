package com.navyattack.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MenuView {
    //Definición de variables de instancia
    private StackPane root;
    private Scene scene;
    private Button userButton;

    public MenuView() {
        Image userImage = new Image("https://example.com/foto_usuario.png", 90, 90, true, true);
        ImageView imageView = new ImageView(userImage);

        userButton = new Button("Usuario 1");
        userButton.setGraphic(imageView);
        userButton.setContentDisplay(ContentDisplay.LEFT);

        root = new StackPane(userButton);

        scene = new Scene(root, 1920, 1080);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getUserButton() {
        return userButton;
    }
}

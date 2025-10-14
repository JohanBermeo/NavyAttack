package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Application;

import com.navyattack.model.User;
import com.navyattack.controller.NavigationController;

/**
 * Vista de transición entre el deployment de jugadores.
 * Se muestra para que el jugador 2 pueda tomar el control sin ver
 * los barcos del jugador 1.
 */
public class TransitionView extends Application {

    private Scene scene;
    private User nextPlayer;
    private String gameMode;
    private NavigationController menuController;

    public TransitionView(NavigationController controller, User nextPlayer, String gameMode) {
        this.menuController = controller;
        this.nextPlayer = nextPlayer;
        this.gameMode = gameMode;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #5872C9;");

        // Título
        Label title = new Label("🎮 PLAYER TRANSITION 🎮");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(javafx.scene.paint.Color.WHITE);

        // Mensaje de transición
        VBox messageBox = createMessageBox();

        // Botón para continuar
        Button btnContinue = new Button("I'M READY - START DEPLOYMENT");
        btnContinue.setPrefWidth(300);
        btnContinue.setPrefHeight(50);
        styleButton(btnContinue);
        btnContinue.setOnAction(e -> handleContinue(primaryStage));

        root.getChildren().addAll(title, messageBox, btnContinue);

        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NavyAttack - Player Transition");
        primaryStage.show();
    }

    private VBox createMessageBox() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;"
        );
        box.setMaxWidth(600);

        Label instruction1 = new Label("Player 1 has finished deploying ships!");
        instruction1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        instruction1.setTextFill(javafx.scene.paint.Color.WHITE);

        Label instruction2 = new Label("Now it's " + nextPlayer.getUsername() + "'s turn");
        instruction2.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        instruction2.setTextFill(javafx.scene.paint.Color.YELLOW);

        Label instruction3 = new Label("⚠️ Make sure Player 1 is not looking at the screen!");
        instruction3.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        instruction3.setTextFill(javafx.scene.paint.Color.web("#FFB6C1"));
        instruction3.setWrapText(true);
        instruction3.setMaxWidth(500);

        Label instruction4 = new Label("Click the button below when you're ready to place your ships");
        instruction4.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        instruction4.setTextFill(javafx.scene.paint.Color.WHITE);
        instruction4.setWrapText(true);
        instruction4.setMaxWidth(500);

        box.getChildren().addAll(instruction1, instruction2, instruction3, instruction4);
        return box;
    }

    private void styleButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #45a049;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;"
        ));
    }

    private void handleContinue(Stage stage) {
        // Navegar al deployment del jugador 2
        menuController.navigateToSecondPlayerDeployment(gameMode);
    }

    public Scene getScene() {
        return scene;
    }
}
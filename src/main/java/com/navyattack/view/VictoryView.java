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

import com.navyattack.controller.MenuController;
import com.navyattack.model.User;

/**
 * Vista que se muestra al finalizar una partida.
 * Muestra el ganador y estadísticas del juego.
 */
public class VictoryView extends Application {

    private Scene scene;
    private MenuController menuController;
    private User winner;
    private User loser;
    private String gameMode;
    private int totalTurns;

    public VictoryView(MenuController controller, User winner, User loser, String gameMode, int totalTurns) {
        this.menuController = controller;
        this.winner = winner;
        this.loser = loser;
        this.gameMode = gameMode;
        this.totalTurns = totalTurns;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e);");

        // Animación/Icono de victoria
        Label victoryIcon = new Label("🏆");
        victoryIcon.setFont(Font.font("Arial", 80));

        // Título
        Label title = new Label("VICTORY!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setTextFill(javafx.scene.paint.Color.web("#f39c12"));

        // Nombre del ganador
        String winnerName = winner != null ? winner.getUsername() : "Player";
        Label winnerLabel = new Label(winnerName + " WINS!");
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        winnerLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        // Estadísticas
        VBox statsBox = createStatsBox();

        // Botones
        VBox buttonsBox = createButtonsBox();

        root.getChildren().addAll(victoryIcon, title, winnerLabel, statsBox, buttonsBox);

        scene = new Scene(root, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NavyAttack - Victory!");
        primaryStage.show();
    }

    private VBox createStatsBox() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;"
        );
        box.setMaxWidth(500);

        Label statsTitle = new Label("GAME STATISTICS");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        statsTitle.setTextFill(javafx.scene.paint.Color.YELLOW);

        Label modeLabel = createStatLabel("Game Mode: " + (gameMode.equals("PVC") ? "Player vs CPU" : "Player vs Player"));
        Label turnsLabel = createStatLabel("Total Turns: " + totalTurns);

        String loserName = loser != null ? loser.getUsername() : "CPU";
        Label loserLabel = createStatLabel("Defeated: " + loserName);

        box.getChildren().addAll(statsTitle, modeLabel, turnsLabel, loserLabel);
        return box;
    }

    private Label createStatLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        return label;
    }

    private VBox createButtonsBox() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        Button btnPlayAgain = new Button("PLAY AGAIN");
        btnPlayAgain.setPrefWidth(200);
        styleButton(btnPlayAgain, "#27ae60", "#229954");
        btnPlayAgain.setOnAction(e -> handlePlayAgain());

        Button btnMainMenu = new Button("MAIN MENU");
        btnMainMenu.setPrefWidth(200);
        styleButton(btnMainMenu, "#3498db", "#2980b9");
        btnMainMenu.setOnAction(e -> menuController.navigateToGameMenu());

        box.getChildren().addAll(btnPlayAgain, btnMainMenu);
        return box;
    }

    private void styleButton(Button btn, String normalColor, String hoverColor) {
        btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;"
        ));
    }

    private void handlePlayAgain() {
        menuController.navigateToPlay();
    }

    public Scene getScene() {
        return scene;
    }
}
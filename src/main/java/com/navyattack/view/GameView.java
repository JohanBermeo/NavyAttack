package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Application;

import com.navyattack.view.components.BoardGridComponent;
import com.navyattack.controller.MenuController;
import com.navyattack.model.User;

/**
 * Vista principal del juego durante la batalla.
 * Muestra dos tableros: uno propio (con barcos visibles) y uno enemigo (para atacar).
 */
public class GameView extends Application {

    private Scene scene;
    private MenuController menuController;

    // Componentes de tablero
    private BoardGridComponent myBoard;
    private BoardGridComponent enemyBoard;

    // Labels informativos
    private Label currentTurnLabel;
    private Label messageLabel;
    private Label myScoreLabel;
    private Label enemyScoreLabel;
    private Label myPlayerNameLabel;
    private Label enemyPlayerNameLabel;
    private Label myBoardTitleLabel;
    private Label enemyBoardTitleLabel;

    // Botones
    private Button btnEndTurn;
    private Button btnSurrender;

    // Información del juego
    private User player1;
    private User player2;
    private String gameMode;

    public GameView(MenuController controller, User player1, User player2, String gameMode) {
        this.menuController = controller;
        this.player1 = player1;
        this.player2 = player2;
        this.gameMode = gameMode;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");

        // Top: Información de turno y mensajes
        VBox topPanel = createTopPanel();
        root.setTop(topPanel);

        // Center: Los dos tableros
        HBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);

        // Bottom: Botones de control
        HBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);

        scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NavyAttack - Battle!");
        primaryStage.show();
    }

    private VBox createTopPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #34495e;");

        currentTurnLabel = new Label("CURRENT TURN: PLAYER 1");
        currentTurnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        currentTurnLabel.setTextFill(javafx.scene.paint.Color.YELLOW);

        messageLabel = new Label("Select a cell on the enemy board to attack!");
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        messageLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        panel.getChildren().addAll(currentTurnLabel, messageLabel);
        return panel;
    }

    private HBox createCenterPanel() {
        HBox panel = new HBox(40);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20));

        // Tablero propio (izquierda) - NO interactivo
        VBox myBoardPanel = createBoardPanel(
                "YOUR FLEET",
                player1 != null ? player1.getUsername() : "Player 1",
                true,   // isMyBoard = true
                true    // showShips = true (no se usa actualmente)
        );

        // Tablero enemigo (derecha) - ✓ SÍ interactivo
        VBox enemyBoardPanel = createBoardPanel(
                "ENEMY WATERS",
                player2 != null ? player2.getUsername() : (gameMode.equals("PVC") ? "CPU" : "Player 2"),
                false,  // isMyBoard = false (esto hace que sea interactivo)
                false   // showShips = false
        );

        panel.getChildren().addAll(myBoardPanel, enemyBoardPanel);
        return panel;
    }

    private VBox createBoardPanel(String title, String playerName, boolean isMyBoard, boolean showShips) {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;"
        );

        // Crear label dinámico para el título
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        // Guardar referencia
        if (isMyBoard) {
            myBoardTitleLabel = titleLabel;
        } else {
            enemyBoardTitleLabel = titleLabel;
        }

        // Crear label dinámico para el nombre
        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(javafx.scene.paint.Color.YELLOW);

        // Guardar referencia al label
        if (isMyBoard) {
            myPlayerNameLabel = nameLabel;
        } else {
            enemyPlayerNameLabel = nameLabel;
        }

        boolean interactive = !isMyBoard;
        BoardGridComponent board = new BoardGridComponent(10, interactive);

        System.out.println("Created board: " + title + " (interactive: " + interactive + ")");

        if (isMyBoard) {
            myBoard = board;
            myScoreLabel = new Label("Ships Remaining: 10");
        } else {
            enemyBoard = board;
            enemyScoreLabel = new Label("Ships Remaining: 10");
        }

        Label scoreLabel = isMyBoard ? myScoreLabel : enemyScoreLabel;
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        scoreLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        panel.getChildren().addAll(titleLabel, nameLabel, board.getGridPane(), scoreLabel);
        return panel;
    }

    private HBox createBottomPanel() {
        HBox panel = new HBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #34495e;");

        btnEndTurn = new Button("END TURN");
        btnEndTurn.setPrefWidth(150);
        btnEndTurn.setDisable(true);  // Deshabilitado hasta hacer un ataque
        styleButton(btnEndTurn, "#3498db", "#2980b9");

        btnSurrender = new Button("SURRENDER");
        btnSurrender.setPrefWidth(150);
        styleButton(btnSurrender, "#e74c3c", "#c0392b");
        btnSurrender.setOnAction(e -> handleSurrender());

        panel.getChildren().addAll(btnEndTurn, btnSurrender);
        return panel;
    }

    private void styleButton(Button btn, String normalColor, String hoverColor) {
        btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 10px 20px;"
        );
        btn.setOnMouseEntered(e -> {
            if (!btn.isDisabled()) {
                btn.setStyle(
                        "-fx-background-color: " + hoverColor + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 10px;" +
                                "-fx-padding: 10px 20px;" +
                                "-fx-cursor: hand;"
                );
            }
        });
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 10px 20px;"
        ));
    }

    private void handleSurrender() {
        // Mostrar diálogo de confirmación y volver al menú
        menuController.navigateToGameMenu();
    }

    public void updateMyPlayerName(String name) {
        if (myPlayerNameLabel != null) {
            myPlayerNameLabel.setText(name);
        }
    }

    public void updateEnemyPlayerName(String name) {
        if (enemyPlayerNameLabel != null) {
            enemyPlayerNameLabel.setText(name);
        }
    }

    public void updateMyBoardTitle(String title) {
        if (myBoardTitleLabel != null) {
            myBoardTitleLabel.setText(title);
        }
    }

    public void updateEnemyBoardTitle(String title) {
        if (enemyBoardTitleLabel != null) {
            enemyBoardTitleLabel.setText(title);
        }
    }

    // ===== GETTERS Y SETTERS =====

    public Scene getScene() {
        return scene;
    }

    public BoardGridComponent getMyBoard() {
        return myBoard;
    }

    public BoardGridComponent getEnemyBoard() {
        return enemyBoard;
    }

    public void setOnEnemyBoardClick(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        enemyBoard.setCellClickHandler(handler);
    }

    public void setOnEndTurn(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnEndTurn.setOnAction(handler);
    }

    public void updateCurrentTurn(String playerName) {
        currentTurnLabel.setText("CURRENT TURN: " + playerName.toUpperCase());
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ?
                javafx.scene.paint.Color.web("#e74c3c") :
                javafx.scene.paint.Color.WHITE
        );
    }

    public void updateMyScore(int shipsRemaining) {
        myScoreLabel.setText("Ships Remaining: " + shipsRemaining);
    }

    public void updateEnemyScore(int shipsRemaining) {
        enemyScoreLabel.setText("Ships Remaining: " + shipsRemaining);
    }

    public void enableEndTurnButton(boolean enable) {
        btnEndTurn.setDisable(!enable);
    }

    public void disableEnemyBoard() {
        enemyBoard.disableAllCells();
    }

    public void enableEnemyBoard() {
        enemyBoard.enableAllCells();
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public String getGameMode() {
        return gameMode;
    }
}
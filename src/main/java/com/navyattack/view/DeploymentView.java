package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Application;

import com.navyattack.view.components.BoardGridComponent;
import com.navyattack.controller.MenuController;
import com.navyattack.model.ShipType;
import com.navyattack.model.User;

/**
 * Vista para la fase de deployment (colocación de barcos).
 * Esta vista se muestra ANTES de iniciar la batalla.
 */
public class DeploymentView extends Application {

    private Scene scene;
    private MenuController menuController;
    private BoardGridComponent boardGrid;

    // Botones de deployment
    private Button btnDeployCarrier;
    private Button btnDeployCruiser;
    private Button btnDeployDestroyer;
    private Button btnDeploySubmarine;
    private Button btnRotateShip;
    private Button btnPlaceShip;
    private Button btnStartGame;
    private Button btnBack;

    // Labels de contadores
    private Label[] shipCountLabels;

    // Label de mensajes
    private Label messageLabel;

    private String gameMode; // "PVP" o "PVC"

    private User currentPlayer;

    public DeploymentView(MenuController controller, String gameMode, User currentPlayer) {
        this.menuController = controller;
        this.gameMode = gameMode;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void start(Stage primaryStage) {
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #5872C9;");

        // Panel izquierdo: Tablero
        VBox leftPanel = createBoardPanel();

        // Panel derecho: Controles
        VBox rightPanel = createControlPanel();

        mainLayout.getChildren().addAll(leftPanel, rightPanel);

        scene = new Scene(mainLayout, 1080, 720);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NavyAttack - Deploy Your Ships");
        primaryStage.show();
    }

    private VBox createBoardPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);

        // Mostrar nombre del jugador
        String playerName = currentPlayer != null ? currentPlayer.getUsername() : "Player";
        Label playerLabel = new Label(playerName.toUpperCase() + "'S FLEET");
        playerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        playerLabel.setTextFill(javafx.scene.paint.Color.YELLOW);

        Label title = new Label("PLACE YOUR SHIPS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(javafx.scene.paint.Color.WHITE);

        boardGrid = new BoardGridComponent(10, true);

        panel.getChildren().addAll(playerLabel, title, boardGrid.getGridPane());
        return panel;
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 10;");
        panel.setMinWidth(300);

        Label instructionLabel = new Label("Select a ship and place it on the board");
        instructionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        instructionLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        instructionLabel.setWrapText(true);

        // Crear botones de barcos
        shipCountLabels = new Label[4];

        VBox shipsContainer = new VBox(10);
        shipsContainer.getChildren().addAll(
                createShipButton(ShipType.CARRY, 0),
                createShipButton(ShipType.CRUISER, 1),
                createShipButton(ShipType.DESTROYER, 2),
                createShipButton(ShipType.SUBMARINE, 3)
        );

        // Botones de acción
        HBox rotateBox = new HBox(10);
        rotateBox.setAlignment(Pos.CENTER);

        btnRotateShip = new Button("Rotate");
        btnPlaceShip = new Button("Place Ship");
        styleActionButton(btnRotateShip);
        styleActionButton(btnPlaceShip);
        btnPlaceShip.setDisable(true);

        rotateBox.getChildren().addAll(btnRotateShip, btnPlaceShip);

        // Botón de iniciar juego (deshabilitado al inicio)
        btnStartGame = new Button("START GAME");
        btnStartGame.setMaxWidth(Double.MAX_VALUE);
        btnStartGame.setDisable(true);
        styleStartButton(btnStartGame);

        // Mensaje de estado
        messageLabel = new Label("");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        messageLabel.setTextFill(javafx.scene.paint.Color.YELLOW);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(280);

        // Botón de volver
        btnBack = new Button("Back to Menu");
        btnBack.setMaxWidth(Double.MAX_VALUE);
        UtilsMenuView.styleButton(btnBack, "white", "#EDEDED", "black", "10px 0 10px 0");
        btnBack.setOnAction(e -> menuController.navigateToGameMenu());

        panel.getChildren().addAll(
                instructionLabel,
                shipsContainer,
                rotateBox,
                messageLabel,
                btnStartGame,
                btnBack
        );

        return panel;
    }

    private HBox createShipButton(ShipType type, int index) {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);

        // Crear placeholder visual para el barco
        Pane shipIcon = createShipIcon(type);

        Button btn = new Button("Deploy " + type.name());
        btn.setMinWidth(150);
        styleShipButton(btn);

        Label countLabel = new Label("x" + getInitialShipCount(type));
        countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        countLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        shipCountLabels[index] = countLabel;

        // Guardar referencia según el tipo
        switch (type) {
            case CARRY -> btnDeployCarrier = btn;
            case CRUISER -> btnDeployCruiser = btn;
            case DESTROYER -> btnDeployDestroyer = btn;
            case SUBMARINE -> btnDeploySubmarine = btn;
        }

        container.getChildren().addAll(shipIcon, btn, countLabel);
        return container;
    }

    /**
     * Crea un icono visual simple para representar un barco.
     *
     * @param type Tipo de barco
     * @return Pane con la representación visual del barco
     */
    private Pane createShipIcon(ShipType type) {
        // Dimensiones del icono
        double width = 40;
        double height = 20;

        // Crear un StackPane para el icono
        StackPane icon = new StackPane();
        icon.setPrefSize(width, height);
        icon.setMaxSize(width, height);
        icon.setMinSize(width, height);

        // Color según el tipo de barco
        String color = switch (type) {
            case CARRY -> "#8B4513";    // Marrón
            case CRUISER -> "#708090";     // Gris pizarra
            case DESTROYER -> "#2F4F4F";   // Verde oscuro
            case SUBMARINE -> "#191970";   // Azul medianoche
        };

        // Estilo del icono
        icon.setStyle(String.format(
                "-fx-background-color: %s;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: #FFFFFF;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;",
                color
        ));

        // Agregar un label con el símbolo del barco
        Label symbol = new Label("⛴");
        symbol.setFont(Font.font("Arial", 14));
        symbol.setTextFill(javafx.scene.paint.Color.WHITE);

        icon.getChildren().add(symbol);

        return icon;
    }

    /**
     * Obtiene la cantidad inicial de barcos según el tipo.
     *
     * @param type Tipo de barco
     * @return Cantidad inicial
     */
    private int getInitialShipCount(ShipType type) {
        return switch (type) {
            case CARRY -> 1;
            case CRUISER -> 2;
            case DESTROYER -> 3;
            case SUBMARINE -> 4;
        };
    }

    private void styleShipButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #0F1157;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 8px 16px;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #f0f0f0;" +
                        "-fx-text-fill: #0F1157;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #0F1157;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 8px 16px;"
        ));
    }

    private void styleActionButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #2c2c2c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-padding: 10px 20px;"
        );
        btn.setOnMouseEntered(e -> {
            if (!btn.isDisabled()) {
                btn.setStyle(
                        "-fx-background-color: #3c3c3c;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 15px;" +
                                "-fx-padding: 10px 20px;" +
                                "-fx-cursor: hand;"
                );
            }
        });
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #2c2c2c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-padding: 10px 20px;"
        ));
    }

    private void styleStartButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 15px 30px;"
        );
        btn.setOnMouseEntered(e -> {
            if (!btn.isDisabled()) {
                btn.setStyle(
                        "-fx-background-color: #45a049;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 10px;" +
                                "-fx-padding: 15px 30px;" +
                                "-fx-cursor: hand;"
                );
            }
        });
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 15px 30px;"
        ));
    }

    // ===== GETTERS PARA EL CONTROLADOR =====

    public Scene getScene() {
        return scene;
    }

    public BoardGridComponent getBoardGrid() {
        return boardGrid;
    }

    public void setOnDeployCarrier(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeployCarrier.setOnAction(handler);
    }

    public void setOnDeployCruiser(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeployCruiser.setOnAction(handler);
    }

    public void setOnDeployDestroyer(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeployDestroyer.setOnAction(handler);
    }

    public void setOnDeploySubmarine(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeploySubmarine.setOnAction(handler);
    }

    public void setOnRotateShip(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnRotateShip.setOnAction(handler);
    }

    public void setOnPlaceShip(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnPlaceShip.setOnAction(handler);
    }

    public void setOnStartGame(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnStartGame.setOnAction(handler);
    }

    public void updateShipCount(ShipType type, int count) {
        int index = type.ordinal();
        if (index >= 0 && index < shipCountLabels.length) {
            shipCountLabels[index].setText("x" + count);

            // Deshabilitar botón si no hay más barcos
            Button btn = switch (type) {
                case CARRY -> btnDeployCarrier;
                case CRUISER -> btnDeployCruiser;
                case DESTROYER -> btnDeployDestroyer;
                case SUBMARINE -> btnDeploySubmarine;
            };
            btn.setDisable(count == 0);
        }
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ?
                javafx.scene.paint.Color.web("#ff6b6b") :
                javafx.scene.paint.Color.web("#90EE90")
        );
    }

    public void enablePlaceButton(boolean enable) {
        btnPlaceShip.setDisable(!enable);
    }

    public void enableStartGameButton(boolean enable) {
        btnStartGame.setDisable(!enable);
        if (enable) {
            showMessage("All ships placed! Ready to battle!", false);
        }
    }

    public String getGameMode() {
        return gameMode;
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }
}
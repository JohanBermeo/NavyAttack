package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.navyattack.model.ShipType;
import com.navyattack.controller.NavigationController;
import com.navyattack.view.components.BoardGridComponent;

/**
 * Vista para la fase de deployment (colocación de barcos) en NavyAttack.
 * Esta vista se muestra antes de iniciar la batalla y permite al jugador
 * colocar manualmente sus barcos en el tablero o generar una configuración aleatoria.
 * 
 * Proporciona una interfaz interactiva con un tablero para colocar barcos,
 * botones para seleccionar tipos de barcos, controles de rotación y colocación,
 * y contadores que muestran cuántos barcos de cada tipo quedan por colocar.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class DeploymentView implements IView {

    /**
     * Escena de JavaFX que contiene la vista de deployment.
     */
    private Scene scene;
    
    /**
     * Componente de cuadrícula del tablero donde se colocan los barcos.
     */
    private BoardGridComponent boardGrid;
    
    /**
     * Controlador de navegación para cambiar entre vistas.
     */
    private NavigationController menuController;

    /**
     * Botón para volver al menú principal.
     */
    private Button btnBack;
    
    /**
     * Botón para generar un tablero con barcos colocados aleatoriamente.
     */
    private Button btnRandom;
    
    /**
     * Botón para iniciar el juego después de colocar todos los barcos.
     */
    private Button btnStartGame;
    
    /**
     * Botón para confirmar la colocación del barco seleccionado.
     */
    private Button btnPlaceShip;
    
    /**
     * Botón para rotar el barco seleccionado entre horizontal y vertical.
     */
    private Button btnRotateShip;
    
    /**
     * Botón para seleccionar y desplegar un portaviones.
     */
    private Button btnDeployCarrier;
    
    /**
     * Botón para seleccionar y desplegar un crucero.
     */
    private Button btnDeployCruiser;
    
    /**
     * Botón para seleccionar y desplegar un destructor.
     */
    private Button btnDeployDestroyer;
    
    /**
     * Botón para seleccionar y desplegar un submarino.
     */
    private Button btnDeploySubmarine;

    /**
     * Array de etiquetas que muestran la cantidad restante de cada tipo de barco.
     */
    private Label[] shipCountLabels;

    /**
     * Modo de juego actual (PVP o PVC).
     */
    private String gameMode;
    
    /**
     * Etiqueta para mostrar mensajes de estado y errores al usuario.
     */
    private Label messageLabel;
    
    /**
     * Nombre del jugador actual realizando el deployment.
     */
    private String currentPlayer;

    /**
     * Constructor de la vista de deployment.
     * 
     * @param controller Controlador de navegación
     * @param gameMode Modo de juego (PVP o PVC)
     * @param currentPlayer Nombre del jugador actual
     */
    public DeploymentView(NavigationController controller, String gameMode, String currentPlayer) {
        this.menuController = controller;
        this.gameMode = gameMode;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Inicia y muestra la vista de deployment en el stage proporcionado.
     * 
     * @param primaryStage Stage donde se mostrará la vista
     */
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

    /**
     * Crea el panel izquierdo que contiene el tablero de juego.
     * 
     * @return VBox con el tablero y títulos
     */
    private VBox createBoardPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);

        // Mostrar nombre del jugador
        String playerName = currentPlayer != null ? currentPlayer : "Player";
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

    /**
     * Crea el panel derecho que contiene los controles de deployment.
     * Incluye botones de selección de barcos, controles de rotación y colocación,
     * y opciones adicionales como tablero aleatorio.
     * 
     * @return VBox con todos los controles
     */
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
        btnBack.setOnAction(e -> menuController.navigateToView("menu"));

        // Botón de tablero aleatorio
        btnRandom = new Button("Random board");
        btnRandom.setMaxWidth(Double.MAX_VALUE);
        btnRandom.setStyle(
            "-fx-background-color: linear-gradient(to right, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
             "-fx-padding: 10px 0 10px 0;" +
            "-fx-border-color: #FFFFFF;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;"
        );
        
        btnRandom.setOnMouseEntered(e -> {
                btnRandom.setStyle(
                    "-fx-background-color: linear-gradient(to left, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 10px 0 10px 0;" +
                    "-fx-border-color: #FFFFFF;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;"
                );
                btnRandom.setCursor(Cursor.HAND);
            }
        );
        
        btnRandom.setOnMouseExited(e -> 
            btnRandom.setStyle(
                "-fx-background-color: linear-gradient(to right, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 10px 0 10px 0;" +
                "-fx-border-color: #FFFFFF;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;"
            )
        );

        panel.getChildren().addAll(
                instructionLabel,
                shipsContainer,
                rotateBox,
                messageLabel,
                btnStartGame,
                btnRandom,
                btnBack
        );

        return panel;
    }

    /**
     * Crea un botón para seleccionar un tipo específico de barco.
     * Incluye un icono visual del barco, el botón de deployment y un contador.
     * 
     * @param type Tipo de barco
     * @param index Índice del barco en el array de contadores
     * @return HBox con el botón completo de selección de barco
     */
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
     * Crea un icono visual simple para representar un tipo de barco.
     * Cada tipo de barco tiene un color distintivo.
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
     * Obtiene la cantidad inicial de barcos disponibles según el tipo.
     * 
     * @param type Tipo de barco
     * @return Cantidad inicial de barcos de ese tipo
     */
    private int getInitialShipCount(ShipType type) {
        return switch (type) {
            case CARRY -> 1;
            case CRUISER -> 2;
            case DESTROYER -> 3;
            case SUBMARINE -> 4;
        };
    }

    /**
     * Aplica estilo visual a los botones de selección de barco.
     * Incluye efectos hover para mejorar la experiencia de usuario.
     * 
     * @param btn Botón a estilizar
     */
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

    /**
     * Aplica estilo visual a los botones de acción (rotar y colocar).
     * 
     * @param btn Botón a estilizar
     */
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

    /**
     * Aplica estilo visual al botón de iniciar juego.
     * 
     * @param btn Botón a estilizar
     */
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

    /**
     * Obtiene la escena de esta vista.
     * 
     * @return Escena de JavaFX
     */
    @Override
    public Scene getScene() {
        return scene;
    }

    /**
     * Obtiene el componente de cuadrícula del tablero.
     * 
     * @return BoardGridComponent del tablero
     */
    public BoardGridComponent getBoardGrid() {
        return boardGrid;
    }

    /**
     * Establece el manejador de eventos para el botón de desplegar portaviones.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnDeployCarrier(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeployCarrier.setOnAction(handler);
    }

    /**
     * Establece el manejador de eventos para el botón de desplegar crucero.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnDeployCruiser(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeployCruiser.setOnAction(handler);
    }

    /**
     * Establece el manejador de eventos para el botón de desplegar destructor.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnDeployDestroyer(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeployDestroyer.setOnAction(handler);
    }

    /**
     * Establece el manejador de eventos para el botón de desplegar submarino.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnDeploySubmarine(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnDeploySubmarine.setOnAction(handler);
    }

    /**
     * Establece el manejador de eventos para el botón de rotar barco.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnRotateShip(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnRotateShip.setOnAction(handler);
    }

    /**
     * Establece el manejador de eventos para el botón de colocar barco.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnPlaceShip(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnPlaceShip.setOnAction(handler);
    }

    /**
     * Establece el manejador de eventos para el botón de iniciar juego.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnStartGame(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnStartGame.setOnAction(handler);
    }

    /**
     * Establece el manejador de eventos para el botón de tablero aleatorio.
     * 
     * @param handler Manejador de eventos
     */
    public void setOnRandomBoard(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnRandom.setOnAction(handler);
    }

    /**
     * Actualiza el contador de barcos disponibles para un tipo específico.
     * Deshabilita el botón de deployment si no quedan barcos de ese tipo.
     * 
     * @param type Tipo de barco
     * @param count Nueva cantidad de barcos disponibles
     */
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

    /**
     * Muestra un mensaje al usuario en la etiqueta de estado.
     * 
     * @param message Mensaje a mostrar
     * @param isError true si es un mensaje de error, false si es informativo
     */
    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ?
                javafx.scene.paint.Color.web("#ff6b6b") :
                javafx.scene.paint.Color.web("#90EE90")
        );
    }

    /**
     * Habilita o deshabilita el botón de colocar barco.
     * 
     * @param enable true para habilitar, false para deshabilitar
     */
    public void enablePlaceButton(boolean enable) {
        btnPlaceShip.setDisable(!enable);
    }

    /**
     * Habilita o deshabilita el botón de iniciar juego.
     * Muestra un mensaje de confirmación cuando se habilita.
     * 
     * @param enable true para habilitar, false para deshabilitar
     */
    public void enableStartGameButton(boolean enable) {
        btnStartGame.setDisable(!enable);
        if (enable) {
            showMessage("All ships placed! Ready to battle!", false);
        }
    }

    /**
     * Obtiene el modo de juego actual.
     * 
     * @return Modo de juego (PVP o PVC)
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * Obtiene el nombre del jugador actual.
     * 
     * @return Nombre del jugador
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
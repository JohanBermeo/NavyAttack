package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.FontWeight;

import com.navyattack.controller.NavigationController;
import com.navyattack.view.components.BoardGridComponent;

/**
 * Vista principal del juego durante la batalla.
 * Muestra dos tableros: uno propio (con barcos visibles) y uno enemigo (para atacar).
 * 
 * @author Juan Manuel Otálora Hernández - 
 *         Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class GameView implements IView {

    /** Escena principal del juego */
    private Scene scene;

    /** Controlador de navegación entre vistas */
    private NavigationController menuController;

    /** Tablero propio del jugador */
    private BoardGridComponent myBoard;

    /** Tablero enemigo para los ataques */
    private BoardGridComponent enemyBoard;

    /** Etiqueta con el turno actual */
    private Label currentTurnLabel;

    /** Etiqueta para mensajes informativos */
    private Label messageLabel;

    /** Etiqueta con la puntuación del jugador */
    private Label myScoreLabel;

    /** Etiqueta con la puntuación del enemigo */
    private Label enemyScoreLabel;

    /** Etiqueta con el nombre del jugador */
    private Label myPlayerNameLabel;

    /** Etiqueta con el nombre del enemigo */
    private Label enemyPlayerNameLabel;

    /** Título del tablero propio */
    private Label myBoardTitleLabel;

    /** Título del tablero enemigo */
    private Label enemyBoardTitleLabel;

    /** Etiqueta del cronómetro */
    private Label timerLabel;

    /** Botón para finalizar el turno */
    private Button btnEndTurn;

    /** Botón para rendirse */
    private Button btnSurrender;

    /** Nombre del jugador 1 */
    private String player1;

    /** Nombre del jugador 2 */
    private String player2;

    /** Modo de juego actual */
    private String gameMode;

    /**
     * Constructor de la vista del juego.
     * 
     * @param controller Controlador de navegación
     * @param player1    Nombre del jugador 1
     * @param player2    Nombre del jugador 2
     * @param gameMode   Modo de juego seleccionado
     */
    public GameView(NavigationController controller, String player1, String player2, String gameMode) {
        this.menuController = controller;
        this.player1 = player1;
        this.player2 = player2;
        this.gameMode = gameMode;
    }

    /**
     * Inicia la vista principal del juego.
     * 
     * @param primaryStage Escenario principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");

        VBox topPanel = createTopPanel();
        root.setTop(topPanel);

        HBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);

        HBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);

        scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NavyAttack - Battle!");
        primaryStage.show();
    }

    /**
     * Crea el panel superior con etiquetas informativas y el cronómetro.
     * 
     * @return Panel superior (VBox)
     */
    private VBox createTopPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #34495e;");

        timerLabel = new Label("⏱ 00:00");
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        timerLabel.setTextFill(javafx.scene.paint.Color.web("#3498db"));

        currentTurnLabel = new Label("CURRENT TURN: PLAYER 1");
        currentTurnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        currentTurnLabel.setTextFill(javafx.scene.paint.Color.YELLOW);

        messageLabel = new Label("Select a cell on the enemy board to attack!");
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        messageLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        panel.getChildren().addAll(timerLabel, currentTurnLabel, messageLabel);
        return panel;
    }

    /**
     * Crea el panel central con los tableros del jugador y del enemigo.
     * 
     * @return Panel central (HBox)
     */
    private HBox createCenterPanel() {
        HBox panel = new HBox(40);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20));

        VBox myBoardPanel = createBoardPanel("YOUR FLEET", player1, true, true);
        VBox enemyBoardPanel = createBoardPanel("ENEMY WATERS", player2 != null ? player2 : "CPU", false, false);

        panel.getChildren().addAll(myBoardPanel, enemyBoardPanel);
        return panel;
    }

    /**
     * Crea un panel de tablero individual (jugador o enemigo).
     * 
     * @param title      Título del tablero
     * @param playerName Nombre del jugador
     * @param isMyBoard  Indica si el tablero es propio
     * @param showShips  Indica si se muestran los barcos
     * @return Panel del tablero (VBox)
     */
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

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        if (isMyBoard) {
            myBoardTitleLabel = titleLabel;
        } else {
            enemyBoardTitleLabel = titleLabel;
        }

        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(javafx.scene.paint.Color.YELLOW);

        if (isMyBoard) {
            myPlayerNameLabel = nameLabel;
        } else {
            enemyPlayerNameLabel = nameLabel;
        }

        boolean interactive = !isMyBoard;
        BoardGridComponent board = new BoardGridComponent(10, interactive);

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

    /**
     * Crea el panel inferior con los botones de control del juego.
     * 
     * @return Panel inferior (HBox)
     */
    private HBox createBottomPanel() {
        HBox panel = new HBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #34495e;");

        btnEndTurn = new Button("END TURN");
        btnEndTurn.setPrefWidth(150);
        btnEndTurn.setDisable(true);
        styleButton(btnEndTurn, "#3498db", "#2980b9");

        btnSurrender = new Button("SURRENDER");
        btnSurrender.setPrefWidth(150);
        styleButton(btnSurrender, "#e74c3c", "#c0392b");
        btnSurrender.setOnAction(e -> handleSurrender());

        panel.getChildren().addAll(btnEndTurn, btnSurrender);
        return panel;
    }

    /**
     * Aplica estilos a un botón y define su comportamiento al pasar el cursor.
     * 
     * @param btn         Botón a estilizar
     * @param normalColor Color normal
     * @param hoverColor  Color al pasar el cursor
     */
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

    /**
     * Maneja la acción de rendirse, regresando al menú principal.
     */
    private void handleSurrender() {
        menuController.navigateToView("menu");
    }

    /**
     * Actualiza el nombre del jugador propio.
     * 
     * @param name Nombre del jugador
     */
    public void updateMyPlayerName(String name) {
        if (myPlayerNameLabel != null) {
            myPlayerNameLabel.setText(name);
        }
    }

    /**
     * Actualiza el nombre del jugador enemigo.
     * 
     * @param name Nombre del enemigo
     */
    public void updateEnemyPlayerName(String name) {
        if (enemyPlayerNameLabel != null) {
            enemyPlayerNameLabel.setText(name);
        }
    }

    /**
     * Actualiza el título del tablero propio.
     * 
     * @param title Nuevo título
     */
    public void updateMyBoardTitle(String title) {
        if (myBoardTitleLabel != null) {
            myBoardTitleLabel.setText(title);
        }
    }

    /**
     * Actualiza el título del tablero enemigo.
     * 
     * @param title Nuevo título
     */
    public void updateEnemyBoardTitle(String title) {
        if (enemyBoardTitleLabel != null) {
            enemyBoardTitleLabel.setText(title);
        }
    }

    /**
     * Enlaza la propiedad del cronómetro a una etiqueta.
     * 
     * @param timeProperty Propiedad con el tiempo del cronómetro
     */
    public void bindTimer(javafx.beans.property.StringProperty timeProperty) {
        timerLabel.textProperty().bind(
                javafx.beans.binding.Bindings.concat("⏱ ", timeProperty)
        );
    }

    /** {@inheritDoc} */
    @Override
    public Scene getScene() {
        return scene;
    }

    /** @return Tablero del jugador */
    public BoardGridComponent getMyBoard() {
        return myBoard;
    }

    /** @return Tablero del enemigo */
    public BoardGridComponent getEnemyBoard() {
        return enemyBoard;
    }

    /**
     * Asigna el manejador de clics para las celdas del tablero enemigo.
     * 
     * @param handler Manejador del evento
     */
    public void setOnEnemyBoardClick(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        enemyBoard.setCellClickHandler(handler);
    }

    /**
     * Asigna el manejador de evento para finalizar turno.
     * 
     * @param handler Manejador del evento
     */
    public void setOnEndTurn(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        btnEndTurn.setOnAction(handler);
    }

    /**
     * Actualiza el texto del turno actual.
     * 
     * @param playerName Nombre del jugador en turno
     */
    public void updateCurrentTurn(String playerName) {
        currentTurnLabel.setText("CURRENT TURN: " + playerName.toUpperCase());
    }

    /**
     * Muestra un mensaje informativo o de error en pantalla.
     * 
     * @param message Texto del mensaje
     * @param isError Indica si el mensaje es de error
     */
    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ?
                javafx.scene.paint.Color.web("#e74c3c") :
                javafx.scene.paint.Color.WHITE
        );
    }

    /**
     * Actualiza la cantidad de barcos restantes del jugador.
     * 
     * @param shipsRemaining Número de barcos restantes
     */
    public void updateMyScore(int shipsRemaining) {
        myScoreLabel.setText("Ships Remaining: " + shipsRemaining);
    }

    /**
     * Actualiza la cantidad de barcos restantes del enemigo.
     * 
     * @param shipsRemaining Número de barcos restantes
     */
    public void updateEnemyScore(int shipsRemaining) {
        enemyScoreLabel.setText("Ships Remaining: " + shipsRemaining);
    }

    /**
     * Habilita o deshabilita el botón de finalizar turno.
     * 
     * @param enable true para habilitar, false para deshabilitar
     */
    public void enableEndTurnButton(boolean enable) {
        btnEndTurn.setDisable(!enable);
    }

    /** Deshabilita la interacción con el tablero enemigo. */
    public void disableEnemyBoard() {
        enemyBoard.disableAllCells();
    }

    /** Habilita la interacción con el tablero enemigo. */
    public void enableEnemyBoard() {
        enemyBoard.enableAllCells();
    }

    /** @return Nombre del jugador 1 */
    public String getPlayer1() {
        return player1;
    }

    /** @return Nombre del jugador 2 */
    public String getPlayer2() {
        return player2;
    }

    /** @return Modo de juego actual */
    public String getGameMode() {
        return gameMode;
    }
}

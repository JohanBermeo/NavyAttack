package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.navyattack.controller.NavigationController;

/**
 * Vista que se muestra al finalizar una partida.
 * Presenta al jugador ganador, las estadísticas generales del enfrentamiento
 * y un resumen de desempeño de ambos jugadores.
 *
 * <p>Permite al usuario regresar al menú principal o iniciar una nueva partida.</p>
 * 
 * @author Juan Manuel Otálora Hernández - 
 *         Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class VictoryView implements IView {

    private String loser;
    private Scene scene;
    private String winner;
    private int totalTurns;
    private String gameMode;
    private String timePlayed;
    private int loserShipsSunk;
    private int winnerShipsSunk;
    private NavigationController menuController;

    /**
     * Crea una vista de victoria con la información final del juego.
     *
     * @param controller       controlador de navegación para cambiar entre vistas
     * @param winner           nombre del jugador ganador
     * @param loser            nombre del jugador perdedor
     * @param gameMode         modo de juego utilizado (PVC o PVP)
     * @param totalTurns       número total de turnos jugados
     * @param timePlayed       tiempo total de juego en formato legible
     * @param timePlayedMillis tiempo total de juego en milisegundos
     * @param winnerShipsSunk  número de barcos destruidos por el ganador
     * @param loserShipsSunk   número de barcos destruidos por el perdedor
     */
    public VictoryView(NavigationController controller, String winner, String loser, String gameMode,
                       int totalTurns, String timePlayed, long timePlayedMillis,
                       int winnerShipsSunk, int loserShipsSunk) {
        this.menuController = controller;
        this.winner = winner;
        this.loser = loser;
        this.gameMode = gameMode;
        this.totalTurns = totalTurns;
        this.timePlayed = timePlayed;
        this.winnerShipsSunk = winnerShipsSunk;
        this.loserShipsSunk = loserShipsSunk;
    }

    /**
     * Inicia la vista de victoria, construyendo la interfaz gráfica
     * con los elementos de resumen y botones de navegación.
     *
     * @param primaryStage escenario principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e);");

        Label victoryIcon = new Label("🏆");
        victoryIcon.setFont(Font.font("Arial", 80));

        Label title = new Label("VICTORY!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setTextFill(javafx.scene.paint.Color.web("#f39c12"));

        String winnerName = winner != null ? winner : "Player";
        Label winnerLabel = new Label(winnerName + " WINS!");
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        winnerLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        VBox generalStatsBox = createGeneralStatsBox();
        HBox playersStatsBox = createPlayersStatsBox();
        VBox buttonsBox = createButtonsBox();

        root.getChildren().addAll(victoryIcon, title, winnerLabel, generalStatsBox, playersStatsBox, buttonsBox);

        scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NavyAttack - Victory!");
        primaryStage.show();
    }

    /**
     * Crea el contenedor de estadísticas generales del juego.
     *
     * @return un {@link VBox} con el resumen de modo, duración y turnos.
     */
    private VBox createGeneralStatsBox() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;"
        );
        box.setMaxWidth(600);

        Label statsTitle = new Label("⚔ BATTLE SUMMARY ⚔");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        statsTitle.setTextFill(javafx.scene.paint.Color.YELLOW);

        GridPane statsGrid = new GridPane();
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);

        addStatRow(statsGrid, 0, "🎮 Game Mode:",
                gameMode.equals("PVC") ? "Player vs CPU" : "Player vs Player");
        addStatRow(statsGrid, 1, "⏱ Game Duration:", timePlayed);
        addStatRow(statsGrid, 2, "🔄 Total Turns:", String.valueOf(totalTurns));

        box.getChildren().addAll(statsTitle, statsGrid);
        return box;
    }

    /**
     * Construye el contenedor de estadísticas individuales
     * de cada jugador (ganador y perdedor).
     *
     * @return un {@link HBox} con las tarjetas de estadísticas de ambos jugadores.
     */
    private HBox createPlayersStatsBox() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 0, 10, 0));

        String winnerName = winner != null ? winner : "Player";
        String loserName = loser != null ? loser : (gameMode.equals("PVC") ? "CPU" : "Player 2");

        VBox winnerStats = createPlayerStatsCard(winnerName, winnerShipsSunk, loserShipsSunk, true);

        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        vsLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        vsLabel.setPadding(new Insets(40, 0, 0, 0));

        VBox loserStats = createPlayerStatsCard(loserName, loserShipsSunk, winnerShipsSunk, false);

        container.getChildren().addAll(winnerStats, vsLabel, loserStats);
        return container;
    }

    /**
     * Genera una tarjeta visual con las estadísticas de un jugador.
     *
     * @param playerName nombre del jugador
     * @param shipsSunk  cantidad de barcos destruidos
     * @param shipsLost  cantidad de barcos perdidos
     * @param isWinner   indica si el jugador fue el ganador
     * @return un {@link VBox} con la tarjeta de estadísticas del jugador
     */
    private VBox createPlayerStatsCard(String playerName, int shipsSunk, int shipsLost, boolean isWinner) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);

        String backgroundColor = isWinner ? "rgba(39, 174, 96, 0.2)" : "rgba(231, 76, 60, 0.2)";
        String borderColor = isWinner ? "#27ae60" : "#e74c3c";

        card.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;"
        );

        Label icon = new Label(isWinner ? "👑" : "💀");
        icon.setFont(Font.font("Arial", 32));

        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nameLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        Label resultLabel = new Label(isWinner ? "WINNER" : "DEFEATED");
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultLabel.setTextFill(javafx.scene.paint.Color.web(isWinner ? "#27ae60" : "#e74c3c"));

        javafx.scene.control.Separator separator = new javafx.scene.control.Separator();
        separator.setPrefWidth(200);

        VBox statsBox = new VBox(8);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(10, 20, 10, 20));

        Label shipsSunkLabel = createStatLabel("⚓ Ships Destroyed: " + shipsSunk, "#3498db");
        Label shipsLostLabel = createStatLabel("💥 Ships Lost: " + shipsLost, "#e74c3c");

        statsBox.getChildren().addAll(shipsSunkLabel, shipsLostLabel);

        card.getChildren().addAll(icon, nameLabel, resultLabel, separator, statsBox);
        return card;
    }

    /**
     * Agrega una fila de estadística al panel de resumen.
     *
     * @param grid  panel donde se insertará la fila
     * @param row   número de fila
     * @param label descripción de la estadística
     * @param value valor correspondiente
     */
    private void addStatRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelNode.setTextFill(javafx.scene.paint.Color.web("#ecf0f1"));

        Label valueNode = new Label(value);
        valueNode.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        valueNode.setTextFill(javafx.scene.paint.Color.WHITE);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    /**
     * Crea una etiqueta de estadística individual estilizada.
     *
     * @param text  texto de la etiqueta
     * @param color color del texto
     * @return un {@link Label} con el estilo configurado
     */
    private Label createStatLabel(String text, String color) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.setTextFill(javafx.scene.paint.Color.web(color));
        return label;
    }

    /**
     * Construye el contenedor de botones de acción (jugar nuevamente o volver al menú).
     *
     * @return un {@link VBox} con los botones configurados
     */
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
        btnMainMenu.setOnAction(e -> menuController.navigateToView("menu"));

        box.getChildren().addAll(btnPlayAgain, btnMainMenu);
        return box;
    }

    /**
     * Aplica estilo visual a un botón, incluyendo color base y efecto hover.
     *
     * @param btn          botón a estilizar
     * @param normalColor  color de fondo por defecto
     * @param hoverColor   color de fondo al pasar el cursor
     */
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

    /**
     * Redirige al usuario a la vista de juego para comenzar una nueva partida.
     */
    private void handlePlayAgain() {
        menuController.navigateToView("play");
    }

    /**
     * Obtiene la escena generada por esta vista.
     *
     * @return la {@link Scene} actual
     */
    @Override
    public Scene getScene() {
        return scene;
    }
}

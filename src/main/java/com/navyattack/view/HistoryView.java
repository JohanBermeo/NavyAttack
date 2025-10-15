package com.navyattack.view;

import java.util.List;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import com.navyattack.model.History;
import com.navyattack.model.UserStatistics;
import com.navyattack.controller.NavigationController;

/**
 * Vista que muestra el historial de partidas jugadas por un usuario.
 * Incluye estadísticas generales, partidas pasadas y opción para volver al menú principal.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class HistoryView implements IView {

    /** Escena principal de la vista */
    private Scene scene;

    /** Nombre del usuario actual */
    private String username;

    /** Lista de partidas jugadas */
    private List<History> history;

    /** Controlador de navegación entre vistas */
    private NavigationController controller;

    /** Lista visual que muestra las partidas del historial */
    private ListView<History> historyListView;

    /**
     * Constructor de la vista del historial.
     * 
     * @param controller Controlador de navegación entre vistas
     * @param username   Nombre del usuario actual
     * @param history    Lista de partidas jugadas por el usuario
     */
    public HistoryView(NavigationController controller, String username, List<History> history) {
        this.history = history;
        this.username = username;
        this.controller = controller;
    }

    /**
     * Inicializa y muestra la interfaz del historial.
     * 
     * @param primaryStage Ventana principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NavyAttack - History");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        VBox topPanel = createTopPanel();
        root.setTop(topPanel);

        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);

        HBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);

        scene = new Scene(root, 700, 650);
        scene.getRoot().setStyle("-fx-background-color: #5872C9;");
        primaryStage.setScene(scene);
        primaryStage.show();
        loadHistories();
    }

    /**
     * Crea el panel superior con el título y estadísticas del usuario.
     * 
     * @return Panel superior con información del usuario
     */
    private VBox createTopPanel() {
        VBox topPanel = new VBox(15);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(0, 0, 15, 0));

        Label titleLabel = new Label("History - " + username);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox statsPanel = createStatsPanel();

        topPanel.getChildren().addAll(titleLabel, statsPanel);
        return topPanel;
    }

    /**
     * Crea el panel con las estadísticas del usuario.
     * 
     * @return Panel con estadísticas (juegos, victorias, derrotas, etc.)
     */
    private HBox createStatsPanel() {
        HBox panel = new HBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;"
        );

        UserStatistics stats = new UserStatistics(username, history);

        VBox gamesBox = createStatBox("🎮", "Games", String.valueOf(stats.getTotalGames()));
        VBox winsBox = createStatBox("🏆", "Wins", String.valueOf(stats.getVictories()));
        VBox lossesBox = createStatBox("💀", "Losses", String.valueOf(stats.getDefeats()));
        VBox winRateBox = createStatBox("📊", "Win Rate", String.format("%.1f%%", stats.getWinRate()));
        VBox shipsSunkBox = createStatBox("⚓", "Ships Sunk", String.valueOf(stats.getTotalShipsSunk()));
        VBox timeBox = createStatBox("⏱", "Total Time", stats.getTotalPlayTimeFormatted());

        panel.getChildren().addAll(gamesBox, winsBox, lossesBox, winRateBox, shipsSunkBox, timeBox);
        return panel;
    }

    /**
     * Crea una caja individual de estadística.
     * 
     * @param icon  Icono representativo
     * @param label Etiqueta de la estadística
     * @param value Valor numérico o textual
     * @return Caja de estadística formateada
     */
    private VBox createStatBox(String icon, String label, String value) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #E8E8E8;");

        box.getChildren().addAll(iconLabel, valueLabel, textLabel);
        return box;
    }

    /**
     * Crea el panel central que muestra el historial de partidas.
     * 
     * @return Panel central con lista de partidas
     */
    private VBox createCenterPanel() {
        VBox centerPanel = new VBox();
        centerPanel.setAlignment(Pos.TOP_CENTER);
        centerPanel.setPadding(new Insets(10, 0, 0, 0));

        Label historyTitle = new Label("📜 MATCH HISTORY");
        historyTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        historyTitle.setPadding(new Insets(0, 0, 10, 0));

        StackPane historyContainer = new StackPane();
        historyContainer.setPrefWidth(650);
        historyContainer.setPrefHeight(350);

        historyListView = new ListView<>();
        historyListView.setPrefWidth(650);
        historyListView.setPrefHeight(350);
        historyListView.setStyle("-fx-background-color: #5872C9; -fx-border-color: #FFFFFF; -fx-border-width: 3;");
        historyListView.setCellFactory(listView -> new HistoryCardCell());

        VBox emptyHistoryView = createEmptyHistoryView();
        historyListView.setPlaceholder(emptyHistoryView);

        historyContainer.getChildren().add(historyListView);
        centerPanel.getChildren().addAll(historyTitle, historyContainer);
        return centerPanel;
    }

    /**
     * Crea la vista que se muestra cuando el historial está vacío.
     * 
     * @return Panel con mensaje informativo
     */
    private VBox createEmptyHistoryView() {
        VBox emptyView = new VBox();
        emptyView.setAlignment(Pos.CENTER);
        emptyView.setSpacing(15);
        emptyView.setStyle("-fx-background-color: transparent;");

        Label icon = new Label("🎯");
        icon.setStyle("-fx-font-size: 48px;");

        Label title = new Label("No game history");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");

        Label subtitle = new Label("Play your first game to see your history here!");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #E8E8E8;");

        emptyView.getChildren().addAll(icon, title, subtitle);
        return emptyView;
    }

    /**
     * Carga las partidas del historial en la lista visual.
     */
    private void loadHistories() {
        if (history != null) {
            historyListView.getItems().clear();
            java.util.List<History> reversed = new java.util.ArrayList<>(history);
            java.util.Collections.reverse(reversed);
            historyListView.getItems().addAll(reversed);
        }
    }

    /**
     * Crea el panel inferior con el botón de retorno al menú principal.
     * 
     * @return Panel inferior con botón "Return"
     */
    private HBox createBottomPanel() {
        HBox bottomPanel = new HBox();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(15, 0, 0, 0));

        Button backButton = new Button("Return");
        backButton.setPrefWidth(150);
        UtilsMenuView.styleButton(backButton, "black", "#333333", "white", "5px 0 5px 0");
        backButton.setOnAction(e -> controller.navigateToView("menu"));

        bottomPanel.getChildren().add(backButton);
        return bottomPanel;
    }

    /**
     * Clase interna que define el formato visual de cada partida del historial.
     */
    private class HistoryCardCell extends ListCell<History> {

        /**
         * Actualiza el contenido visual de cada elemento de la lista.
         * 
         * @param history Objeto de tipo History a representar
         * @param empty   Indica si la celda está vacía
         */
        @Override
        protected void updateItem(History history, boolean empty) {
            super.updateItem(history, empty);

            if (empty || history == null) {
                setGraphic(null);
                setStyle("-fx-background-color: transparent;");
            } else {
                VBox card = createHistoryCard(history);
                setGraphic(card);
                setStyle("-fx-background-color: transparent;");
            }
        }

        /**
         * Crea la tarjeta visual que representa una partida.
         * 
         * @param history Objeto History con los datos de la partida
         * @return Tarjeta con la información del resultado y estadísticas
         */
        private VBox createHistoryCard(History history) {
            VBox card = new VBox(10);
            card.setPadding(new Insets(15));

            boolean playerWon = history.didPlayerWin(username);
            String backgroundColor = playerWon ? "#e8f5e9" : "#ffebee";
            String borderColor = playerWon ? "#4caf50" : "#f44336";

            card.setStyle(String.format(
                    "-fx-background-color: %s;" +
                    "-fx-border-color: %s;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 10;" +
                    "-fx-background-radius: 10;",
                    backgroundColor, borderColor
            ));

            HBox header = new HBox();
            header.setAlignment(Pos.CENTER);
            header.setSpacing(15);

            Label resultLabel = new Label(playerWon ? "✅ VICTORY" : "❌ DEFEAT");
            resultLabel.setStyle(String.format(
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 16px;" +
                    "-fx-text-fill: %s;",
                    playerWon ? "#2e7d32" : "#c62828"
            ));

            Label dateLabel = new Label("📅 " + history.getGameDateFormatted());
            dateLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            header.getChildren().addAll(resultLabel, spacer, dateLabel);

            HBox playersSection = createPlayersSection(history, playerWon);
            GridPane statsGrid = createMatchStatsGrid(history);

            card.getChildren().addAll(header, new Separator(), playersSection, statsGrid);
            return card;
        }

        /**
         * Crea la sección que muestra los jugadores de la partida.
         * 
         * @param history   Objeto History con los datos del encuentro
         * @param playerWon Indica si el usuario ganó
         * @return Panel con nombres e iconos de los jugadores
         */
        private HBox createPlayersSection(History history, boolean playerWon) {
            HBox section = new HBox(20);
            section.setAlignment(Pos.CENTER);
            section.setPadding(new Insets(5, 0, 5, 0));

            VBox playerBox = new VBox(3);
            playerBox.setAlignment(Pos.CENTER);

            Label playerIcon = new Label(playerWon ? "👑" : "💀");
            playerIcon.setStyle("-fx-font-size: 20px;");

            Label playerName = new Label(username);
            playerName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: black;");

            playerBox.getChildren().addAll(playerIcon, playerName);

            Label vsLabel = new Label("VS");
            vsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #666666;");

            VBox opponentBox = new VBox(3);
            opponentBox.setAlignment(Pos.CENTER);

            Label opponentIcon = new Label(playerWon ? "💀" : "👑");
            opponentIcon.setStyle("-fx-font-size: 20px;");

            Label opponentName = new Label(history.getLoser().equals(username) ?
                    history.getWinner() : history.getLoser());
            opponentName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: black;");

            opponentBox.getChildren().addAll(opponentIcon, opponentName);

            section.getChildren().addAll(playerBox, vsLabel, opponentBox);
            return section;
        }

        /**
         * Crea la cuadrícula que muestra las estadísticas de la partida.
         * 
         * @param history Objeto History con los datos de la partida
         * @return Cuadrícula con estadísticas detalladas
         */
        private GridPane createMatchStatsGrid(History history) {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(25);
            grid.setVgap(5);
            grid.setPadding(new Insets(10, 0, 0, 0));

            boolean playerWon = history.didPlayerWin(username);
            int playerShipsSunk = playerWon ? history.getWinnerShipsSunk() : history.getLoserShipsSunk();
            int opponentShipsSunk = playerWon ? history.getLoserShipsSunk() : history.getWinnerShipsSunk();

            addStatToGrid(grid, 0, 0, "🎮", history.getGameMode().equals("PVC") ? "vs CPU" : "vs Player");
            addStatToGrid(grid, 1, 0, "⏱", history.getTimePlayed());
            addStatToGrid(grid, 2, 0, "🔄", history.getTotalTurns() + " turns");
            addStatToGrid(grid, 0, 1, "⚓", playerShipsSunk + " sunk");
            addStatToGrid(grid, 1, 1, "💥", opponentShipsSunk + " lost");

            return grid;
        }

        /**
         * Añade una estadística individual al grid.
         * 
         * @param grid  Cuadrícula destino
         * @param col   Columna
         * @param row   Fila
         * @param icon  Icono representativo
         * @param value Valor textual
         */
        private void addStatToGrid(GridPane grid, int col, int row, String icon, String value) {
            HBox box = new HBox(5);
            box.setAlignment(Pos.CENTER);

            Label iconLabel = new Label(icon);
            iconLabel.setStyle("-fx-font-size: 14px;");

            Label valueLabel = new Label(value);
            valueLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333;");

            box.getChildren().addAll(iconLabel, valueLabel);
            grid.add(box, col, row);
        }
    }

    /**
     * Retorna la escena principal de la vista.
     * 
     * @return Objeto Scene de la vista
     */
    public Scene getScene() {
        return scene;
    }
}

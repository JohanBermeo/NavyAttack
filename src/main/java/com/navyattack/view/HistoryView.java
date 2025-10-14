package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.application.Application;

import com.navyattack.model.User;
import com.navyattack.model.History;
import com.navyattack.model.UserStatistics;
import com.navyattack.controller.MenuController;

public class HistoryView extends Application {

    private Scene scene;
    private User currentUser;
    private MenuController controller;
    private ListView<History> historyListView;

    public HistoryView(MenuController controller, User user) {
        this.controller = controller;
        this.currentUser = user;
    }

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

    private VBox createTopPanel() {
        VBox topPanel = new VBox(15);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(0, 0, 15, 0));

        Label titleLabel = new Label("History - " + (currentUser != null ? currentUser.getUsername() : "N/A"));
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // ✓ NUEVO: Panel de estadísticas generales
        HBox statsPanel = createStatsPanel();

        topPanel.getChildren().addAll(titleLabel, statsPanel);
        return topPanel;
    }

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

        if (currentUser == null) {
            return panel;
        }

        UserStatistics stats = new UserStatistics(currentUser);

        // Juegos totales
        VBox gamesBox = createStatBox("🎮", "Games", String.valueOf(stats.getTotalGames()));

        // Victorias
        VBox winsBox = createStatBox("🏆", "Wins", String.valueOf(stats.getVictories()));

        // Derrotas
        VBox lossesBox = createStatBox("💀", "Losses", String.valueOf(stats.getDefeats()));

        // Win Rate
        VBox winRateBox = createStatBox("📊", "Win Rate", String.format("%.1f%%", stats.getWinRate()));

        // Barcos hundidos
        VBox shipsSunkBox = createStatBox("⚓", "Ships Sunk", String.valueOf(stats.getTotalShipsSunk()));

        // Tiempo total
        VBox timeBox = createStatBox("⏱", "Total Time", stats.getTotalPlayTimeFormatted());

        panel.getChildren().addAll(gamesBox, winsBox, lossesBox, winRateBox, shipsSunkBox, timeBox);
        return panel;
    }

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

    private void loadHistories() {
        if (currentUser != null && currentUser.getHistory() != null) {
            historyListView.getItems().clear();
            // Mostrar las partidas más recientes primero
            java.util.List<History> reversed = new java.util.ArrayList<>(currentUser.getHistory());
            java.util.Collections.reverse(reversed);
            historyListView.getItems().addAll(reversed);
        }
    }

    private HBox createBottomPanel() {
        HBox bottomPanel = new HBox();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(15, 0, 0, 0));

        Button backButton = new Button("Return");
        backButton.setPrefWidth(150);
        UtilsMenuView.styleButton(backButton, "black", "#333333", "white", "5px 0 5px 0");
        backButton.setOnAction(e -> {
            controller.navigateToGameMenu();
        });

        bottomPanel.getChildren().add(backButton);
        return bottomPanel;
    }

    private class HistoryCardCell extends ListCell<History> {

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

        private VBox createHistoryCard(History history) {
            VBox card = new VBox(10);
            card.setPadding(new Insets(15));

            // Determinar si el jugador principal ganó
            boolean playerWon = history.didPlayerWin(currentUser.getUsername());
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

            // Header: Resultado y fecha
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

            // Sección de jugadores
            HBox playersSection = createPlayersSection(history, playerWon);

            // Estadísticas de la partida
            GridPane statsGrid = createMatchStatsGrid(history);

            card.getChildren().addAll(header, new Separator(), playersSection, statsGrid);
            return card;
        }

        private HBox createPlayersSection(History history, boolean playerWon) {
            HBox section = new HBox(20);
            section.setAlignment(Pos.CENTER);
            section.setPadding(new Insets(5, 0, 5, 0));

            // Jugador actual
            VBox playerBox = new VBox(3);
            playerBox.setAlignment(Pos.CENTER);

            Label playerIcon = new Label(playerWon ? "👑" : "💀");
            playerIcon.setStyle("-fx-font-size: 20px;");

            Label playerName = new Label(currentUser.getUsername());
            playerName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: black;");

            playerBox.getChildren().addAll(playerIcon, playerName);

            // VS
            Label vsLabel = new Label("VS");
            vsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #666666;");

            // Oponente
            VBox opponentBox = new VBox(3);
            opponentBox.setAlignment(Pos.CENTER);

            Label opponentIcon = new Label(playerWon ? "💀" : "👑");
            opponentIcon.setStyle("-fx-font-size: 20px;");

            Label opponentName = new Label(history.getLoser().equals(currentUser.getUsername()) ?
                    history.getWinner() : history.getLoser());
            opponentName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: black;");

            opponentBox.getChildren().addAll(opponentIcon, opponentName);

            section.getChildren().addAll(playerBox, vsLabel, opponentBox);
            return section;
        }

        private GridPane createMatchStatsGrid(History history) {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(25);
            grid.setVgap(5);
            grid.setPadding(new Insets(10, 0, 0, 0));

            // Determinar si el jugador ganó para mostrar stats correctas
            boolean playerWon = history.didPlayerWin(currentUser.getUsername());
            int playerShipsSunk = playerWon ? history.getWinnerShipsSunk() : history.getLoserShipsSunk();
            int opponentShipsSunk = playerWon ? history.getLoserShipsSunk() : history.getWinnerShipsSunk();

            // Modo de juego
            addStatToGrid(grid, 0, 0, "🎮", history.getGameMode().equals("PVC") ? "vs CPU" : "vs Player");

            // Tiempo
            addStatToGrid(grid, 1, 0, "⏱", history.getTimePlayed());

            // Turnos
            addStatToGrid(grid, 2, 0, "🔄", history.getTotalTurns() + " turns");

            // Barcos hundidos (jugador)
            addStatToGrid(grid, 0, 1, "⚓", playerShipsSunk + " sunk");

            // Barcos perdidos (jugador)
            addStatToGrid(grid, 1, 1, "💥", opponentShipsSunk + " lost");

            return grid;
        }

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

    public Scene getScene() {
        return scene;
    }
}
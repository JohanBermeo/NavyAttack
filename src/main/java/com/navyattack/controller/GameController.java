package com.navyattack.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import com.navyattack.model.*;
import com.navyattack.view.GameView;
import com.navyattack.view.TurnTransitionView;
import com.navyattack.model.GameTimer;

import java.util.List;

/**
 * Controlador para la fase de batalla del juego.
 * Maneja los turnos, ataques y condiciones de victoria.
 */
public class GameController {

    private final GameView view;
    private final Board player1Board;
    private final Board player2Board;
    private final MenuController menuController;
    private final NavigationController navigationController;

    private int turnCounter;
    private CPU cpu = new CPU();
    private boolean isPlayer1Turn;
    private boolean hasAttackedThisTurn;
    private GameTimer gameTimer;

    public GameController(Board player1Board, Board player2Board, GameView view, MenuController menuController, NavigationController navigationController) {
        this.view = view;
        this.turnCounter = 0;
        this.isPlayer1Turn = true;
        this.hasAttackedThisTurn = false;
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        this.menuController = menuController;
        this.navigationController = navigationController;

        this.gameTimer = new GameTimer();
        view.bindTimer(gameTimer.timeStringProperty());
        gameTimer.start();

        connectHandlers();
        initializeBoards();
        updateTurnDisplay();
    }

    private void connectHandlers() {
        view.setOnEnemyBoardClick(this::handleAttack);
        view.setOnEndTurn(e -> handleEndTurn());
    }

    private void initializeBoards() {
        // ✓ Establecer nombres iniciales correctos
        String player1Name = view.getPlayer1() != null ? view.getPlayer1() : "Player 1";
        String player2Name;

        if (view.getGameMode().equals("PVC")) {
            player2Name = "CPU";
        } else {
            player2Name = view.getPlayer2() != null ? view.getPlayer2() : "Player 2";
        }

        view.updateMyPlayerName(player1Name);
        view.updateEnemyPlayerName(player2Name);

        // Mostrar los barcos del jugador 1 en "mi tablero" (izquierda)
        displayShipsOnBoard(player1Board, view.getMyBoard());

        // Actualizar contadores
        updateScores();
    }

    private void displayShipsOnBoard(Board board, com.navyattack.view.components.BoardGridComponent grid) {
        List<Ship> ships = board.getShips();

        for (Ship ship : ships) {
            if (ship.isPlaced()) {
                List<int[]> positions = ship.getPositions();
                grid.showShipCells(positions);
            }
        }
    }

    private void displayShipsAndAttacksOnBoard(Board board, com.navyattack.view.components.BoardGridComponent grid) {
        List<Ship> ships = board.getShips();

        for (Ship ship : ships) {
            if (ship.isPlaced()) {
                List<int[]> positions = ship.getPositions();
                grid.showShipCells(positions);
            }
        }
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                CellState state = board.getCellState(row, col);

                if (state == CellState.HIT) {
                    grid.markHit(row, col);
                } else if (state == CellState.MISS) {
                    grid.markMiss(row, col);
                }
            }
        }
    }

    private void handleAttack(ActionEvent e) {
        if (hasAttackedThisTurn) {
            view.showMessage("You already attacked this turn! Click 'END TURN' to continue.", true);
            return;
        }

        Object source = e.getSource();
        if (!(source instanceof Button btn)) {
            System.err.println("Source is not a Button");
            return;
        }

        Object userData = btn.getUserData();
        if (!(userData instanceof int[] pos) || pos.length != 2) {
            System.err.println("UserData is not valid position");
            return;
        }

        int row = pos[0];
        int col = pos[1];

        // Determinar qué tablero atacar
        Board targetBoard = isPlayer1Turn ? player2Board : player1Board;

        // Ejecutar ataque
        AttackResult result = targetBoard.attack(row, col);

        // Procesar resultado
        processAttackResult(result, row, col);
    }

    private void processAttackResult(AttackResult result, int row, int col) {
        if (result.isInvalidMove()) {
            view.showMessage(result.getDescription(), true);
            return;
        }

        // Marcar la celda en el tablero enemigo
        if (result == AttackResult.HIT) {
            view.getEnemyBoard().markHit(row, col);
            view.showMessage("🎯 HIT! You damaged an enemy ship!", false);
        } else if (result == AttackResult.SUNK) {
            view.getEnemyBoard().markHit(row, col);
            view.showMessage("💥 SUNK! You destroyed an enemy ship!", false);
        } else if (result == AttackResult.MISS) {
            view.getEnemyBoard().markMiss(row, col);
            view.showMessage("💦 MISS! You hit water.", false);
        }

        // Marcar que ya se atacó en este turno
        hasAttackedThisTurn = true;
        view.enableEndTurnButton(true);
        view.disableEnemyBoard();

        updateScores();

        // Verificar victoria
        Board targetBoard = isPlayer1Turn ? player2Board : player1Board;
        if (targetBoard.areAllShipsSunk()) {
            handleVictory();
        }
    }

    private void handleEndTurn() {
        if (!hasAttackedThisTurn) {
            view.showMessage("You must attack before ending your turn!", true);
            return;
        }

        // Incrementar contador de turnos
        turnCounter++;

        // Cambiar turno (solo para PVP)
        if (view.getGameMode().equals("PVP")) {
            isPlayer1Turn = !isPlayer1Turn;
        } 
        hasAttackedThisTurn = false;

        // Actualizar UI
        view.enableEndTurnButton(false);
        updateTurnDisplay();

        if (view.getGameMode().equals("PVP")) {
            showTurnTransition();
        } else {
            // Turno de la CPU
            int[] posAttack = cpu.attack();
            AttackResult result = player1Board.attack(posAttack[0], posAttack[1]);
           
            // La CPU aprende del resultado para su próximo ataque
            cpu.processResult(result, posAttack);

            // Actualiza el tablero con el ataque de CPU
            displayShipsAndAttacksOnBoard(player1Board, view.getMyBoard());
            view.enableEnemyBoard();
        }
    }

    private void saveGameHistory(String winnerName, String loserName, String timePlayed, long timePlayedMillis,
                                 int winnerShipsSunk, int loserShipsSunk) {

        User winner = menuController.getUserByUsername(winnerName);
        User loser = menuController.getUserByUsername(loserName);

        List<User> players = new java.util.ArrayList<>();
        if (winner != null) players.add(winner);
        if (loser != null && !view.getGameMode().equals("PVC")) players.add(loser);

        History history = new History(
                players,
                winnerName,
                loserName,
                timePlayed,
                timePlayedMillis,
                view.getGameMode(),
                turnCounter,
                winnerShipsSunk,
                loserShipsSunk
        );

        // Agregar al historial de ambos jugadores
        if (winner != null) {
            winner.addHistory(history);
        }

        if (loser != null && !view.getGameMode().equals("PVC")) {
            loser.addHistory(history);
        }

        // Notificar al MenuController para que guarde los datos
        menuController.saveGameData();
    }

    private void pauseTimer() {
        if (gameTimer != null) {
            gameTimer.pause();
        }
    }

    private void resumeTimer() {
        if (gameTimer != null) {
            gameTimer.start();
        }
    }

    private void showTurnTransition() {
        if (view.getGameMode().equals("PVC")) {
            view.enableEnemyBoard();
            return;
        }

        pauseTimer();

        String nextPlayerName = isPlayer1Turn ? view.getPlayer1() : view.getPlayer2();
        Stage stage = (Stage) view.getScene().getWindow();
        Scene gameScene = view.getScene();

        if (stage == null) {
            System.err.println("ERROR: Stage is null!");
            return;
        }

        TurnTransitionView transitionView = new TurnTransitionView(
                stage,
                gameScene,
                nextPlayerName,
                this::continueAfterTransition
        );

        transitionView.show();
    }

    private void continueAfterTransition() {
        resumeTimer();

        swapBoardDisplays();
        view.enableEnemyBoard();
    }

    private void swapBoardDisplays() {
        // Limpiar ambos tableros
        view.getMyBoard().clearAll();
        view.getEnemyBoard().clearAll();

        // Determinar qué jugador es el actual
        String currentPlayer = isPlayer1Turn ? view.getPlayer1() : view.getPlayer2();
        String enemyPlayer = isPlayer1Turn ? view.getPlayer2() : view.getPlayer1();

        // ✓ Actualizar nombres de los jugadores
        String myPlayerName = currentPlayer != null ? currentPlayer : (isPlayer1Turn ? "Player 1" : "Player 2");
        String enemyPlayerName = enemyPlayer != null ? enemyPlayer : (isPlayer1Turn ? "Player 2" : "Player 1");

        // En modo PVC, el enemigo siempre es CPU
        if (view.getGameMode().equals("PVC")) {
            myPlayerName = view.getPlayer1();
            enemyPlayerName = "CPU";
        }

        view.updateMyBoardTitle("YOUR FLEET");
        view.updateEnemyBoardTitle("ENEMY WATERS - " + enemyPlayerName.toUpperCase());

        view.updateMyPlayerName(myPlayerName);
        view.updateEnemyPlayerName(enemyPlayerName);

        // Mostrar barcos del jugador actual en "mi tablero"
        Board currentPlayerBoard = isPlayer1Turn ? player1Board : player2Board;
        displayShipsOnBoard(currentPlayerBoard, view.getMyBoard());

        // Mostrar ataques previos en el tablero enemigo
        Board enemyBoard = isPlayer1Turn ? player2Board : player1Board;
        displayAttacksOnEnemyBoard(enemyBoard, view.getEnemyBoard());

        // Actualizar scores
        updateScores();
    }

    private void displayAttacksOnEnemyBoard(Board board, com.navyattack.view.components.BoardGridComponent grid) {
        // Recorrer todas las celdas y mostrar HITs y MISSes
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                CellState state = board.getCellState(row, col);

                if (state == CellState.HIT) {
                    grid.markHit(row, col);
                } else if (state == CellState.MISS) {
                    grid.markMiss(row, col);
                }
            }
        }
    }

    private void updateTurnDisplay() {
        String currentPlayer = isPlayer1Turn ? view.getPlayer1() :
                (view.getPlayer2() != null ? view.getPlayer2() : "Player 2");

        view.updateCurrentTurn(currentPlayer);
        view.showMessage("Select a cell on the enemy board to attack!", false);
    }

    private void updateScores() {
        int player1Ships = countRemainingShips(player1Board);
        int player2Ships = countRemainingShips(player2Board);

        if (isPlayer1Turn) {
            view.updateMyScore(player1Ships);
            view.updateEnemyScore(player2Ships);
        } else {
            view.updateMyScore(player2Ships);
            view.updateEnemyScore(player1Ships);
        }
    }

    private int countRemainingShips(Board board) {
        int count = 0;
        for (Ship ship : board.getShips()) {
            if (!ship.isSunk()) {
                count++;
            }
        }
        return count;
    }

    private void handleVictory() {
        gameTimer.stop();
        String finalTime = gameTimer.getFormattedTime();
        long finalTimeMillis = gameTimer.getElapsedTimeMillis();

        String winner = isPlayer1Turn ? view.getPlayer1() : view.getPlayer2();
        String loser = isPlayer1Turn ? view.getPlayer2() : view.getPlayer1();

        String winnerName = winner != null ? winner : (isPlayer1Turn ? "Player 1" : "Player 2");
        String loserName = loser != null ? loser : (view.getGameMode().equals("PVC") ? "CPU" : "Player 2");

        // Calcular barcos hundidos
        Board winnerBoard = isPlayer1Turn ? player1Board : player2Board;
        Board loserBoard = isPlayer1Turn ? player2Board : player1Board;

        int winnerShipsSunk = countSunkShips(loserBoard); // Barcos que hundió el ganador
        int loserShipsSunk = countSunkShips(winnerBoard); // Barcos que hundió el perdedor

        view.showMessage("🎉 " + winnerName + " WINS! 🎉", false);
        view.disableEnemyBoard();
        view.enableEndTurnButton(false);

        saveGameHistory(winnerName, loserName, finalTime, finalTimeMillis,
                winnerShipsSunk, loserShipsSunk);

        // Navegar a pantalla de victoria después de 2 segundos
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> {
                    navigationController.navigateToVictory(winner, loser, view.getGameMode(),
                            turnCounter, finalTime, finalTimeMillis,
                            winnerShipsSunk, loserShipsSunk);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int countSunkShips(Board board) {
        int sunkCount = 0;
        for (Ship ship : board.getShips()) {
            if (ship.isSunk()) {
                sunkCount++;
            }
        }
        return sunkCount;
    }
}
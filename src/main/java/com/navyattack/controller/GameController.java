package com.navyattack.controller;

import com.navyattack.model.*;
import com.navyattack.view.GameView;
import com.navyattack.view.TurnTransitionView;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.List;

/**
 * Controlador para la fase de batalla del juego.
 * Maneja los turnos, ataques y condiciones de victoria.
 */
public class GameController {

    private final Board player1Board;
    private final Board player2Board;
    private final GameView view;
    private final MenuController menuController;

    private boolean isPlayer1Turn;
    private boolean hasAttackedThisTurn;
    private int turnCounter;

    public GameController(Board player1Board, Board player2Board, GameView view, MenuController menuController) {
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        this.view = view;
        this.menuController = menuController;
        this.isPlayer1Turn = true;
        this.hasAttackedThisTurn = false;
        this.turnCounter = 0;

        System.out.println("=== GameController Initialized ===");
        System.out.println("Player 1 ships: " + player1Board.getPlacedShipsCount());
        System.out.println("Player 2 ships: " + player2Board.getPlacedShipsCount());

        connectHandlers();
        initializeBoards();
        updateTurnDisplay();
    }

    private void connectHandlers() {
        System.out.println("Connecting handlers...");

        // ✓ CRÍTICO: Conectar el handler de clicks en el tablero enemigo
        view.setOnEnemyBoardClick(this::handleAttack);
        view.setOnEndTurn(e -> handleEndTurn());

        System.out.println("Handlers connected successfully");
    }

    private void initializeBoards() {
        System.out.println("Initializing boards...");

        // ✓ Establecer nombres iniciales correctos
        String player1Name = view.getPlayer1() != null ? view.getPlayer1().getUsername() : "Player 1";
        String player2Name;

        if (view.getGameMode().equals("PVC")) {
            player2Name = "CPU";
        } else {
            player2Name = view.getPlayer2() != null ? view.getPlayer2().getUsername() : "Player 2";
        }

        view.updateMyPlayerName(player1Name);
        view.updateEnemyPlayerName(player2Name);

        // Mostrar los barcos del jugador 1 en "mi tablero" (izquierda)
        displayShipsOnBoard(player1Board, view.getMyBoard());

        // El tablero enemigo (derecha) NO muestra barcos
        // Solo mostrará HITs y MISSes después de atacar

        // Actualizar contadores
        updateScores();

        System.out.println("Boards initialized");
    }

    private void displayShipsOnBoard(Board board, com.navyattack.view.components.BoardGridComponent gridComponent) {
        List<Ship> ships = board.getShips();
        System.out.println("Displaying " + ships.size() + " ships on board");

        for (Ship ship : ships) {
            if (ship.isPlaced()) {
                List<int[]> positions = ship.getPositions();
                gridComponent.showShipCells(positions);
            }
        }
    }

    private void handleAttack(ActionEvent e) {
        System.out.println("=== ATTACK TRIGGERED ===");

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

        System.out.println("Attack at: [" + row + ", " + col + "]");

        // Determinar qué tablero atacar
        Board targetBoard = isPlayer1Turn ? player2Board : player1Board;

        // Ejecutar ataque
        AttackResult result = targetBoard.attack(row, col);

        System.out.println("Attack result: " + result);

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
        System.out.println("=== END TURN ===");

        if (!hasAttackedThisTurn) {
            view.showMessage("You must attack before ending your turn!", true);
            return;
        }

        // Incrementar contador de turnos
        turnCounter++;

        // Cambiar turno
        isPlayer1Turn = !isPlayer1Turn;
        hasAttackedThisTurn = false;

        System.out.println("Turn " + turnCounter + ": Now it's " + (isPlayer1Turn ? "Player 1" : "Player 2") + "'s turn");

        // Actualizar UI
        view.enableEndTurnButton(false);
        updateTurnDisplay();

        // Si es modo PVP, mostrar pantalla de transición de turno
        if (view.getGameMode().equals("PVP")) {
            showTurnTransition();
        } else {
            // Modo PVC: turno de la CPU
            view.enableEnemyBoard();
            // TODO: Implementar IA de CPU (por ahora el jugador sigue atacando)
        }
    }

    private void showTurnTransition() {
        if (view.getGameMode().equals("PVC")) {
            // Modo CPU: no necesita transición
            view.enableEnemyBoard();
            return;
        }

        System.out.println("=== SHOWING TURN TRANSITION ===");

        // Modo PVP: mostrar pantalla de transición
        String nextPlayerName = isPlayer1Turn ?
                (view.getPlayer1() != null ? view.getPlayer1().getUsername() : "Player 1") :
                (view.getPlayer2() != null ? view.getPlayer2().getUsername() : "Player 2");

        // ✓ Obtener Stage y Scene ANTES de crear la transición
        Stage stage = (Stage) view.getScene().getWindow();
        Scene gameScene = view.getScene();

        if (stage == null) {
            System.err.println("ERROR: Stage is null!");
            return;
        }

        System.out.println("Creating transition for: " + nextPlayerName);

        // ✓ Pasar el Stage Y la Scene original
        TurnTransitionView transitionView = new TurnTransitionView(
                stage,
                gameScene,  // ← Pasar la escena del juego
                nextPlayerName,
                this::continueAfterTransition
        );

        transitionView.show();
    }

    private void continueAfterTransition() {
        System.out.println("=== CONTINUING AFTER TRANSITION ===");

        // ✓ Ya no necesitas restaurar la escena aquí porque TurnTransitionView lo hace
        // La escena ya fue restaurada cuando se presionó el botón

        // Intercambiar displays de tableros
        swapBoardDisplays();

        // Habilitar tablero enemigo
        view.enableEnemyBoard();

        System.out.println("Transition complete, board enabled");
    }

    private void swapBoardDisplays() {
        System.out.println("=== SWAPPING BOARD DISPLAYS ===");

        // Limpiar ambos tableros
        view.getMyBoard().clearAll();
        view.getEnemyBoard().clearAll();

        // Determinar qué jugador es el actual
        User currentPlayer = isPlayer1Turn ? view.getPlayer1() : view.getPlayer2();
        User enemyPlayer = isPlayer1Turn ? view.getPlayer2() : view.getPlayer1();

        // ✓ Actualizar nombres de los jugadores
        String myPlayerName = currentPlayer != null ? currentPlayer.getUsername() : (isPlayer1Turn ? "Player 1" : "Player 2");
        String enemyPlayerName = enemyPlayer != null ? enemyPlayer.getUsername() : (isPlayer1Turn ? "Player 2" : "Player 1");

        // En modo PVC, el enemigo siempre es CPU
        if (view.getGameMode().equals("PVC")) {
            myPlayerName = view.getPlayer1() != null ? view.getPlayer1().getUsername() : "Player 1";
            enemyPlayerName = "CPU";
        }

        view.updateMyBoardTitle("YOUR FLEET");
        view.updateEnemyBoardTitle("ENEMY WATERS - " + enemyPlayerName.toUpperCase());

        view.updateMyPlayerName(myPlayerName);
        view.updateEnemyPlayerName(enemyPlayerName);

        System.out.println("My player: " + myPlayerName);
        System.out.println("Enemy player: " + enemyPlayerName);

        // Mostrar barcos del jugador actual en "mi tablero"
        Board currentPlayerBoard = isPlayer1Turn ? player1Board : player2Board;
        displayShipsOnBoard(currentPlayerBoard, view.getMyBoard());

        // Mostrar ataques previos en el tablero enemigo
        Board enemyBoard = isPlayer1Turn ? player2Board : player1Board;
        displayAttacksOnEnemyBoard(enemyBoard, view.getEnemyBoard());

        // Actualizar scores
        updateScores();

        System.out.println("Board displays swapped");
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
        String currentPlayer = isPlayer1Turn ?
                (view.getPlayer1() != null ? view.getPlayer1().getUsername() : "Player 1") :
                (view.getPlayer2() != null ? view.getPlayer2().getUsername() : "Player 2");

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
        System.out.println("=== VICTORY! ===");

        User winner = isPlayer1Turn ? view.getPlayer1() : view.getPlayer2();
        User loser = isPlayer1Turn ? view.getPlayer2() : view.getPlayer1();

        String winnerName = winner != null ? winner.getUsername() : (isPlayer1Turn ? "Player 1" : "Player 2");

        view.showMessage("🎉 " + winnerName + " WINS! 🎉", false);
        view.disableEnemyBoard();
        view.enableEndTurnButton(false);

        System.out.println("Winner: " + winnerName);
        System.out.println("Total turns: " + turnCounter);

        // Navegar a pantalla de victoria después de 2 segundos
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> {
                    menuController.navigateToVictory(winner, loser, view.getGameMode(), turnCounter);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
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
 * Controlador para la fase de batalla del juego NavyAttack.
 * Maneja la lógica de los turnos, ataques, transiciones entre jugadores
 * y condiciones de victoria. Coordina la interacción entre los tableros
 * de los jugadores y la vista del juego.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class GameController {

    /**
     * Vista del juego que muestra los tableros y la interfaz de batalla.
     */
    private final GameView view;
    
    /**
     * Tablero del primer jugador.
     */
    private final Board player1Board;
    
    /**
     * Tablero del segundo jugador o CPU.
     */
    private final Board player2Board;
    
    /**
     * Controlador del menú principal para gestionar datos de usuario.
     */
    private final MenuController menuController;
    
    /**
     * Controlador de navegación para cambiar entre vistas.
     */
    private final NavigationController navigationController;

    /**
     * Contador de turnos transcurridos en la partida.
     */
    private int turnCounter;
    
    /**
     * Instancia de la CPU para el modo jugador vs computadora.
     */
    private CPU cpu = new CPU();
    
    /**
     * Indica si es el turno del jugador 1.
     * true si es turno del jugador 1, false si es turno del jugador 2.
     */
    private boolean isPlayer1Turn;
    
    /**
     * Indica si el jugador actual ya realizó un ataque en este turno.
     */
    private boolean hasAttackedThisTurn;
    
    /**
     * Temporizador del juego para medir el tiempo de partida.
     */
    private GameTimer gameTimer;

    /**
     * Constructor del controlador del juego.
     * Inicializa los tableros, configura los manejadores de eventos
     * y prepara la interfaz para iniciar la batalla.
     * 
     * @param player1Board Tablero del primer jugador
     * @param player2Board Tablero del segundo jugador o CPU
     * @param view Vista del juego
     * @param menuController Controlador del menú para gestionar datos
     * @param navigationController Controlador de navegación
     */
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

    /**
     * Conecta los manejadores de eventos de la vista con los métodos del controlador.
     * Configura los listeners para los ataques y el botón de finalizar turno.
     */
    private void connectHandlers() {
        view.setOnEnemyBoardClick(this::handleAttack);
        view.setOnEndTurn(e -> handleEndTurn());
    }

    /**
     * Inicializa los tableros al comenzar el juego.
     * Establece los nombres de los jugadores, muestra los barcos del jugador actual
     * y actualiza los contadores de puntuación.
     */
    private void initializeBoards() {
        // Establecer nombres iniciales correctos
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

    /**
     * Muestra los barcos de un tablero en el componente de cuadrícula especificado.
     * 
     * @param board Tablero que contiene los barcos a mostrar
     * @param grid Componente de cuadrícula donde se mostrarán los barcos
     */
    private void displayShipsOnBoard(Board board, com.navyattack.view.components.BoardGridComponent grid) {
        List<Ship> ships = board.getShips();

        for (Ship ship : ships) {
            if (ship.isPlaced()) {
                List<int[]> positions = ship.getPositions();
                grid.showShipCells(positions);
            }
        }
    }

    /**
     * Muestra tanto los barcos como los ataques (impactos y fallos) en un tablero.
     * 
     * @param board Tablero que contiene los barcos y estados de ataque
     * @param grid Componente de cuadrícula donde se mostrará la información
     */
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

    /**
     * Maneja el ataque a una celda del tablero enemigo.
     * Valida que no se haya atacado en este turno y procesa el resultado del ataque.
     * 
     * @param e Evento de acción generado por el click en una celda
     */
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

    /**
     * Procesa el resultado de un ataque y actualiza la vista correspondiente.
     * Marca la celda con el resultado (impacto, fallo o hundido) y verifica
     * si se alcanzó la condición de victoria.
     * 
     * @param result Resultado del ataque ejecutado
     * @param row Fila de la celda atacada
     * @param col Columna de la celda atacada
     */
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

    /**
     * Maneja la finalización del turno actual.
     * Valida que se haya realizado un ataque, incrementa el contador de turnos,
     * cambia el turno al siguiente jugador y ejecuta el turno de la CPU si corresponde.
     */
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

    /**
     * Guarda el historial de la partida finalizada en los perfiles de los jugadores.
     * Crea un registro de History con los datos de la partida y lo asocia a ambos jugadores.
     * 
     * @param winnerName Nombre del jugador ganador
     * @param loserName Nombre del jugador perdedor
     * @param timePlayed Tiempo de juego en formato string
     * @param timePlayedMillis Tiempo de juego en milisegundos
     * @param winnerShipsSunk Número de barcos hundidos por el ganador
     * @param loserShipsSunk Número de barcos hundidos por el perdedor
     */
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

    /**
     * Pausa el temporizador del juego.
     */
    private void pauseTimer() {
        if (gameTimer != null) {
            gameTimer.pause();
        }
    }

    /**
     * Reanuda el temporizador del juego.
     */
    private void resumeTimer() {
        if (gameTimer != null) {
            gameTimer.start();
        }
    }

    /**
     * Muestra la pantalla de transición entre turnos en modo PVP.
     * Pausa el temporizador y muestra una pantalla intermedia antes de
     * cambiar al siguiente jugador.
     */
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

    /**
     * Continúa el juego después de la pantalla de transición.
     * Reanuda el temporizador, intercambia los tableros y habilita el tablero enemigo.
     */
    private void continueAfterTransition() {
        resumeTimer();

        swapBoardDisplays();
        view.enableEnemyBoard();
    }

    /**
     * Intercambia la visualización de los tableros entre jugadores.
     * Actualiza los nombres, muestra los barcos del jugador actual
     * y los ataques previos en el tablero enemigo.
     */
    private void swapBoardDisplays() {
        // Limpiar ambos tableros
        view.getMyBoard().clearAll();
        view.getEnemyBoard().clearAll();

        // Determinar qué jugador es el actual
        String currentPlayer = isPlayer1Turn ? view.getPlayer1() : view.getPlayer2();
        String enemyPlayer = isPlayer1Turn ? view.getPlayer2() : view.getPlayer1();

        // Actualizar nombres de los jugadores
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

    /**
     * Muestra los ataques previos (impactos y fallos) en el tablero enemigo.
     * 
     * @param board Tablero enemigo con los estados de las celdas
     * @param grid Componente de cuadrícula donde se mostrarán los ataques
     */
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

    /**
     * Actualiza la visualización del turno actual en la interfaz.
     * Muestra el nombre del jugador actual y un mensaje informativo.
     */
    private void updateTurnDisplay() {
        String currentPlayer = isPlayer1Turn ? view.getPlayer1() :
                (view.getPlayer2() != null ? view.getPlayer2() : "Player 2");

        view.updateCurrentTurn(currentPlayer);
        view.showMessage("Select a cell on the enemy board to attack!", false);
    }

    /**
     * Actualiza los contadores de barcos restantes en la interfaz.
     * Muestra cuántos barcos quedan vivos para cada jugador.
     */
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

    /**
     * Cuenta el número de barcos que aún no han sido hundidos en un tablero.
     * 
     * @param board Tablero a evaluar
     * @return Número de barcos restantes
     */
    private int countRemainingShips(Board board) {
        int count = 0;
        for (Ship ship : board.getShips()) {
            if (!ship.isSunk()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Maneja la condición de victoria del juego.
     * Detiene el temporizador, guarda el historial de la partida
     * y navega a la pantalla de victoria.
     */
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

    /**
     * Cuenta el número de barcos hundidos en un tablero.
     * 
     * @param board Tablero a evaluar
     * @return Número de barcos hundidos
     */
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
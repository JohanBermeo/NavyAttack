package com.navyattack.controller;

import com.navyattack.model.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import com.navyattack.view.DeploymentView;
import com.navyattack.view.components.BoardGridComponent;

import java.util.List;

/**
 * Controlador para la fase de deployment del juego NavyAttack.
 * Coordina la colocación de barcos en el tablero y gestiona la interacción
 * entre el modelo (Board) y la vista (DeploymentView).
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class DeploymentController {

    /**
     * Tablero donde se colocan los barcos del jugador actual.
     */
    private final Board board;

    /**
     * Vista del deployment que muestra la interfaz gráfica.
     */
    private final DeploymentView view;

    /**
     * Tablero del primer jugador en modo PVP.
     * Se utiliza para almacenar temporalmente el tablero del jugador 1
     * mientras el jugador 2 configura su tablero.
     */
    private static Board player1Board = null;

    /**
     * Controlador de navegación para cambiar entre vistas.
     */
    private final NavigationController controller;

    /**
     * Estado actual del deployment, incluyendo el barco seleccionado y su posición.
     */
    private final DeploymentState deploymentState;

    /**
     * Constructor del controlador de deployment.
     * Inicializa el controlador y conecta los manejadores de eventos.
     * 
     * @param board Tablero donde se colocarán los barcos
     * @param view Vista del deployment
     * @param controller Controlador de navegación
     */
    public DeploymentController(Board board, DeploymentView view, NavigationController controller) {
        this.board = board;
        this.view = view;
        this.deploymentState = new DeploymentState();
        this.controller = controller;

        connectHandlers();
        updateShipCounts();
    }

    /**
     * Conecta todos los manejadores de eventos de la vista con los métodos del controlador.
     * Configura los listeners para los botones de despliegue, rotación, colocación y clicks en celdas.
     */
    private void connectHandlers() {
        BoardGridComponent grid = view.getBoardGrid();
        grid.setCellClickHandler(this::handleCellClick);

        view.setOnDeployCarrier(e -> handleDeployShip(ShipType.CARRY));
        view.setOnDeployCruiser(e -> handleDeployShip(ShipType.CRUISER));
        view.setOnDeployDestroyer(e -> handleDeployShip(ShipType.DESTROYER));
        view.setOnDeploySubmarine(e -> handleDeployShip(ShipType.SUBMARINE));
        view.setOnRotateShip(e -> handleRotation());
        view.setOnPlaceShip(e -> handlePlacement());
        view.setOnStartGame(e -> handleStartGame());
        view.setOnRandomBoard(e -> handleStartWithRandomBoard());
    }

    /**
     * Maneja la selección de un tipo de barco para desplegar.
     * Verifica que haya barcos disponibles del tipo seleccionado y actualiza el estado.
     * 
     * @param type Tipo de barco a desplegar
     */
    private void handleDeployShip(ShipType type) {
        if (board.getRemainingShips(type) == 0) {
            view.showMessage("No more " + type.name() + " available", false);
            return;
        }

        Ship ship = new Ship(type);
        deploymentState.selectShip(ship);
        view.showMessage("Click on the board to place your " + type.name(), false);
    }

    /**
     * Maneja el click en una celda del tablero.
     * Valida la posición seleccionada y muestra una previsualización del barco.
     * 
     * @param e Evento de acción generado por el click en la celda
     */
    private void handleCellClick(ActionEvent e) {
        Object source = e.getSource();
        if (!(source instanceof Button btn)) return;

        Object userData = btn.getUserData();
        if (!(userData instanceof int[] pos) || pos.length != 2) return;

        int row = pos[0];
        int col = pos[1];

        if (!deploymentState.hasSelectedShip()) {
            view.showMessage("Please select a ship first", false);
            return;
        }

        deploymentState.setTargetPosition(row, col);

        Ship ship = deploymentState.getSelectedShip();
        List<int[]> positions = ship.calculatePositions(row, col);

        if (board.canPlaceShip(ship, row, col)) {
            view.getBoardGrid().highlightCells(positions, "#90EE90"); // Verde
            view.enablePlaceButton(true);
            view.showMessage("Valid position - click 'Place Ship'", false);
        } else {
            view.getBoardGrid().highlightCells(positions, "#FF6B6B"); // Rojo
            view.enablePlaceButton(false);
            view.showMessage("Invalid position", false);
        }
    }

    /**
     * Maneja la rotación del barco seleccionado.
     * Cambia la orientación entre horizontal y vertical y actualiza la previsualización.
     */
    private void handleRotation() {
        if (!deploymentState.hasSelectedShip()) {
            view.showMessage("No ship selected to rotate", false);
            return;
        }

        deploymentState.rotateShip();

        if (deploymentState.hasTargetPosition()) {
            // Recalcular preview con nueva orientación
            handleCellClick(createMockEvent(deploymentState.getRow(), deploymentState.getCol()));
        }

        view.showMessage("Ship rotated", false);
    }

    /**
     * Maneja la colocación definitiva de un barco en el tablero.
     * Valida la posición, coloca el barco en el tablero y actualiza la vista.
     */
    private void handlePlacement() {
        if (!deploymentState.isReadyToPlace()) {
            view.showMessage("Select a position first", false);
            return;
        }

        try {
            Ship ship = deploymentState.getSelectedShip();
            int row = deploymentState.getRow();
            int col = deploymentState.getCol();

            board.placeShip(ship, row, col);

            view.getBoardGrid().showShipCells(ship.getPositions());
            view.getBoardGrid().clearHighlights();
            view.updateShipCount(ship.getType(), board.getRemainingShips(ship.getType()));
            view.enablePlaceButton(false);

            deploymentState.reset();

            if (isDeploymentComplete()) {
                view.enableStartGameButton(true);
            } else {
                view.showMessage("Ship placed! Select another ship", false);
            }

        } catch (IllegalStateException e) {
            view.showMessage("Error: " + e.getMessage(), false);
        }
    }

    /**
     * Maneja el inicio del juego después de completar el deployment.
     * En modo PVP, coordina el deployment de ambos jugadores.
     * En modo Player vs CPU, navega directamente al juego.
     */
    private void handleStartGame() {
        String gameMode = view.getGameMode();

        if (gameMode.equals("PVP")) {
            if (player1Board == null) {
                // Este es el primer jugador
                player1Board = board;
                controller.navigateToTransition(gameMode);
            } else {
                // Este es el segundo jugador
                Board player2Board = board;
                controller.navigateToGame(player1Board, player2Board, gameMode);
                // Resetear para la próxima partida
                player1Board = null;
            }
        } else {
            // Modo Player vs CPU
            controller.navigateToGame(board, null, gameMode);
        }
    }

    /**
     * Maneja el inicio del juego con un tablero aleatorio.
     * Coloca todos los barcos automáticamente en posiciones aleatorias válidas.
     */
    private void handleStartWithRandomBoard() {
        String gameMode = view.getGameMode();

        if (gameMode.equals("PVP")) {
            if (player1Board == null) {
                // Este es el primer jugador
                player1Board = board;
                Board.placeShipsRandomly(player1Board);
                controller.navigateToTransition(gameMode);
            } else {
                // Este es el segundo jugador
                Board player2Board = board;
                Board.placeShipsRandomly(player2Board);
                controller.navigateToGame(player1Board, player2Board, gameMode);
                // Resetear para la próxima partida
                player1Board = null;
            }
        } else {
            Board.placeShipsRandomly(board);
            controller.navigateToGame(board, null, gameMode);
        }
    }

    /**
     * Verifica si el deployment está completo.
     * El deployment está completo cuando todos los tipos de barcos han sido colocados.
     * 
     * @return true si todos los barcos han sido colocados, false en caso contrario
     */
    private boolean isDeploymentComplete() {
        for (ShipType type : ShipType.values()) {
            if (board.getRemainingShips(type) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Actualiza los contadores de barcos disponibles en la vista.
     * Muestra cuántos barcos de cada tipo quedan por colocar.
     */
    private void updateShipCounts() {
        for (ShipType type : ShipType.values()) {
            view.updateShipCount(type, board.getRemainingShips(type));
        }
    }

    /**
     * Crea un evento mock para simular clicks en celdas.
     * Utilizado para recalcular la previsualización después de rotar un barco.
     * 
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return Evento de acción simulado
     */
    private ActionEvent createMockEvent(int row, int col) {
        Button mockButton = new Button();
        mockButton.setUserData(new int[]{row, col});
        return new ActionEvent(mockButton, null);
    }

    /**
     * Obtiene el tablero asociado a este controlador.
     * 
     * @return Tablero del jugador
     */
    public Board getBoard() {
        return board;
    }
}
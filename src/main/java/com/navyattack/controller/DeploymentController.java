package com.navyattack.controller;

import com.navyattack.model.*;
import com.navyattack.view.DeploymentView;
import com.navyattack.view.components.BoardGridComponent;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.util.List;

/**
 * Controlador para la fase de deployment.
 * Coordina la colocación de barcos en el tablero.
 */
public class DeploymentController {

    private final Board board;
    private final DeploymentView view;
    private final DeploymentState deploymentState;
    private final MenuController menuController;

    public DeploymentController(Board board, DeploymentView view, MenuController menuController) {
        this.board = board;
        this.view = view;
        this.deploymentState = new DeploymentState();
        this.menuController = menuController;

        connectHandlers();
        updateShipCounts();
    }

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
    }

    private void handleDeployShip(ShipType type) {
        if (board.getRemainingShips(type) == 0) {
            view.showMessage("No more " + type.name() + " available", false);
            return;
        }

        Ship ship = new Ship(type);
        deploymentState.selectShip(ship);
        view.showMessage("Click on the board to place your " + type.name(), false);
    }

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

    private void handleStartGame() {
        // Aquí pasamos al GameView
        String gameMode = view.getGameMode();
        menuController.navigateToGame(board, gameMode);
    }

    private boolean isDeploymentComplete() {
        for (ShipType type : ShipType.values()) {
            if (board.getRemainingShips(type) > 0) {
                return false;
            }
        }
        return true;
    }

    private void updateShipCounts() {
        for (ShipType type : ShipType.values()) {
            view.updateShipCount(type, board.getRemainingShips(type));
        }
    }

    // Helper para crear eventos mock (para recalcular preview)
    private ActionEvent createMockEvent(int row, int col) {
        Button mockButton = new Button();
        mockButton.setUserData(new int[]{row, col});
        return new ActionEvent(mockButton, null);
    }

    public Board getBoard() {
        return board;
    }
}
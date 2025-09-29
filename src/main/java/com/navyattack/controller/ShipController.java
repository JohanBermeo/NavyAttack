package com.navyattack.controller;

import com.navyattack.model.Ship;
import com.navyattack.model.Board;
import com.navyattack.model.ShipType;
import com.navyattack.model.Orientation;

public class ShipController {
    private Ship selectedShip;
    private boolean isRotated = false;
    private Board board;

    public ShipController(Board board) {
        this.board = board;
    }

    public void deployShip(Ship ship) {
        if (ship != null && ship.isDeployable()) {
            selectedShip = ship;
            ship.setDeploymentMode(true);
        }
    }

    public void rotateShip() {
        if (selectedShip != null) {
            if (isRotated) {
                selectedShip.setOrientation(Orientation.HORIZONTAL);
            } else {
                selectedShip.setOrientation(Orientation.VERTICAL);
            }
            isRotated = !isRotated;
            // updateShipView(); - Método que debería implementarse en la vista
        }
    }

    public boolean positionShip(int row, int col, Board board) {
        if (selectedShip != null && board.canPlaceShipAt(row, col, selectedShip.getLength(), selectedShip.getOrientation())) {
            board.placeShip(selectedShip, row, col, selectedShip.getLength(), selectedShip.getOrientation());
            selectedShip.setDeploymentMode(false);
            selectedShip = null;
            return true;
        }
        return false;
    }

    public Ship getSelectedShip() {
        return selectedShip;
    }

    public void setSelectedShip(Ship ship) {
        this.selectedShip = ship;
    }

    public boolean isRotated() {
        return isRotated;
    }
}

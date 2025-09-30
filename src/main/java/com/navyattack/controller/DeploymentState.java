package com.navyattack.controller;

import com.navyattack.model.Ship;

/**
 * Encapsula el estado temporal durante la fase de deployment.
 * Este NO es un concepto del dominio del juego, sino del proceso de colocación.
 */
public class DeploymentState {

    private Ship selectedShip;
    private Integer targetRow;
    private Integer targetCol;

    public void selectShip(Ship ship) {
        this.selectedShip = ship;
        this.targetRow = null;
        this.targetCol = null;
    }

    public void setTargetPosition(int row, int col) {
        this.targetRow = row;
        this.targetCol = col;
    }

    public void rotateShip() {
        if (selectedShip != null) {
            selectedShip.rotate();
        }
    }

    public void reset() {
        this.selectedShip = null;
        this.targetRow = null;
        this.targetCol = null;
    }

    // Queries
    public boolean hasSelectedShip() {
        return selectedShip != null;
    }

    public boolean hasTargetPosition() {
        return targetRow != null && targetCol != null;
    }

    public boolean isReadyToPlace() {
        return hasSelectedShip() && hasTargetPosition();
    }

    // Getters
    public Ship getSelectedShip() {
        return selectedShip;
    }

    public Integer getRow() {
        return targetRow;
    }

    public Integer getCol() {
        return targetCol;
    }
}

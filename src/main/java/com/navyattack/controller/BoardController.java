package com.navyattack.controller;

import com.navyattack.model.Board;
import com.navyattack.model.Orientation;
import com.navyattack.model.Ship;
import com.navyattack.view.BoardView;
import javafx.event.ActionEvent;

public class BoardController {
    private final Board model;
    private final BoardView view;
    private Orientation currentOrientation = Orientation.HORIZONTAL;
    private Ship selectedShip = null;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public BoardController(Board model, BoardView view) {
        this.model = model;
        this.view = view;
        connectViewHandlers();
    }

    private void connectViewHandlers() {
        view.setBtnDeployCarryListener(this::onDeployCarry);
        view.setBtnDeployCruiserListener(this::onDeployCruiser);
        view.setBtnDeployDestroyerListener(this::onDeployDestroyer);
        view.setBtnDeploySubmarineListener(this::onDeploySubmarine);
        view.setBtnRotateShipListener(this::onRotateShip);
        view.setBtnPlaceShipListener(this::onPlaceShip);
        view.setCellClickListener(this::onCellClicked);
    }

    private void onDeployCarry(ActionEvent e) {
        // Suponiendo que ShipType.PORTAAVIONES existe y tiene longitud 5
        selectedShip = new Ship(0, 0, 5, currentOrientation, false, null, 0, false);
    }

    private void onDeployCruiser(ActionEvent e) {
        selectedShip = new Ship(0, 0, 4, currentOrientation, false, null, 0, false);
    }

    private void onDeployDestroyer(ActionEvent e) {
        selectedShip = new Ship(0, 0, 3, currentOrientation, false, null, 0, false);
    }

    private void onDeploySubmarine(ActionEvent e) {
        selectedShip = new Ship(0, 0, 2, currentOrientation, false, null, 0, false);
    }

    private void onRotateShip(ActionEvent e) {
        if (selectedShip != null) {
            currentOrientation = (currentOrientation == Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
            selectedShip.setOrientation(currentOrientation);
        }
    }

    private void onPlaceShip(ActionEvent e) {
        if (selectedShip != null && selectedRow != -1 && selectedCol != -1) {
            if (model.canPlaceShipAt(selectedRow, selectedCol, selectedShip.getLength(), currentOrientation)) {
                model.placeShip(selectedShip, selectedRow, selectedCol, selectedShip.getLength(), currentOrientation);
                // Actualizar la vista (puedes agregar un método para pintar el barco)
                // view.paintShip(selectedRow, selectedCol, selectedShip.getLength(), currentOrientation);
                selectedShip = null;
                selectedRow = -1;
                selectedCol = -1;
            }
        }
    }

    private void onCellClicked(ActionEvent e) {
        if (selectedShip == null) return;
        Object src = e.getSource();
        if (src instanceof javafx.scene.control.Button btn) {
            Object data = btn.getUserData();
            if (data instanceof int[] pos && pos.length == 2) {
                selectedRow = pos[0];
                selectedCol = pos[1];
                // Opcional: resaltar la celda seleccionada
                view.highlightCell(selectedRow, selectedCol);
            }
        }
    }
}

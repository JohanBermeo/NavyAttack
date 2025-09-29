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
        initializeShipCounts();
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

    private void initializeShipCounts() {
        int[] shipCounts = model.getShipCounts();
        int[] shipsPlaced = model.getShipsPlaced();
        for (int i = 0; i < shipCounts.length; i++) {
            view.updateShipCount(i, shipCounts[i] - shipsPlaced[i]);
        }
    }

    private void onDeployCarry(ActionEvent e) {
        selectedShip = model.deployCarrier();
        if (selectedShip != null) {
            System.out.println("Portaaviones seleccionado para despliegue");
        } else {
            System.out.println("No hay más portaaviones disponibles");
        }
    }

    private void onDeployCruiser(ActionEvent e) {
        selectedShip = model.deployCruiser();
        if (selectedShip != null) {
            System.out.println("Crucero seleccionado para despliegue");
        } else {
            System.out.println("No hay más cruceros disponibles");
        }
    }

    private void onDeployDestroyer(ActionEvent e) {
        selectedShip = model.deployDestroyer();
        if (selectedShip != null) {
            System.out.println("Destructor seleccionado para despliegue");
        } else {
            System.out.println("No hay más destructores disponibles");
        }
    }

    private void onDeploySubmarine(ActionEvent e) {
        selectedShip = model.deploySubmarine();
        if (selectedShip != null) {
            System.out.println("Submarino seleccionado para despliegue");
        } else {
            System.out.println("No hay más submarinos disponibles");
        }
    }

    private int getShipTypeByLength(int length) {
        return switch (length) {
            case 6 -> 0; // Portaaviones
            case 4 -> 1; // Crucero
            case 3 -> 2; // Destructor
            case 2 -> 3; // Submarino
            default -> -1;
        };
    }

    private void onRotateShip(ActionEvent e) {
        if (selectedShip != null) {
            currentOrientation = (currentOrientation == Orientation.HORIZONTAL) ?
                    Orientation.VERTICAL : Orientation.HORIZONTAL;
            selectedShip.setOrientation(currentOrientation);
            System.out.println("Barco rotado a: " + currentOrientation);

            // Actualizar vista previa si hay una posición seleccionada
            if (selectedRow != -1 && selectedCol != -1) {
                // Calcular nuevas posiciones y resaltar en el tablero
                java.util.List<int[]> positions = new java.util.ArrayList<>();
                int length = selectedShip.getLength();
                if (currentOrientation == Orientation.HORIZONTAL) {
                    for (int c = selectedCol; c < selectedCol + length && c < 10; c++) {
                        positions.add(new int[]{selectedRow, c});
                    }
                } else {
                    for (int r = selectedRow; r < selectedRow + length && r < 10; r++) {
                        positions.add(new int[]{r, selectedCol});
                    }
                }
                view.highlightCells(positions);
            }
        } else {
            System.out.println("No hay barco seleccionado para rotar");
        }
    }

    private void onPlaceShip(ActionEvent e) {
        if (selectedShip != null && selectedRow != -1 && selectedCol != -1) {
            try {
                if (model.canPlaceShipAt(selectedRow, selectedCol, selectedShip.getLength(), currentOrientation)) {
                    model.placeShip(selectedShip, selectedRow, selectedCol, selectedShip.getLength(), currentOrientation);

                    // Actualizar la vista para mostrar el barco colocado
                    view.showShipOnBoard(selectedRow, selectedCol, selectedShip.getLength(), currentOrientation);

                    // Actualizar contadores
                    int shipType = getShipTypeByLength(selectedShip.getLength());
                    if (shipType != -1) {
                        model.incrementShipPlaced(shipType);
                        int[] shipCounts = model.getShipCounts();
                        int[] shipsPlaced = model.getShipsPlaced();
                        view.updateShipCount(shipType, shipCounts[shipType] - shipsPlaced[shipType]);
                    }

                    System.out.println("Barco colocado en (" + selectedRow + ", " + selectedCol + ")");

                    // Verificar si todos los barcos han sido colocados
                    checkGameReady();

                    // Limpiar selecciones
                    selectedShip = null;
                    selectedRow = -1;
                    selectedCol = -1;
                } else {
                    System.out.println("No se puede colocar el barco en esa posicion");
                }
            } catch (Exception ex) {
                System.err.println("Error al colocar barco: " + ex.getMessage());
            }
        } else {
            if (selectedShip == null) {
                System.out.println("No hay barco seleccionado");
            } else {
                System.out.println("No hay posicion seleccionada");
            }
        }
    }

    private void onCellClicked(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof javafx.scene.control.Button btn) {
            Object data = btn.getUserData();
            if (data instanceof int[] pos && pos.length == 2) {
                selectedRow = pos[0];
                selectedCol = pos[1];
                System.out.println("Celda seleccionada: (" + selectedRow + ", " + selectedCol + ")");
                // Calcular posiciones del barco si hay uno seleccionado
                if (selectedShip != null) {
                    java.util.List<int[]> positions = new java.util.ArrayList<>();
                    int length = selectedShip.getLength();
                    if (currentOrientation == Orientation.HORIZONTAL) {
                        for (int c = selectedCol; c < selectedCol + length && c < 10; c++) {
                            positions.add(new int[]{selectedRow, c});
                        }
                    } else {
                        for (int r = selectedRow; r < selectedRow + length && r < 10; r++) {
                            positions.add(new int[]{r, selectedCol});
                        }
                    }
                    view.highlightCells(positions);
                } else {
                    view.highlightCell(selectedRow, selectedCol);
                }
            }
        }
    }

    private void showShipPreview() {
        // Aquí podrías implementar una vista previa del barco
        // Por ahora solo validamos si se puede colocar
        if (model.canPlaceShipAt(selectedRow, selectedCol, selectedShip.getLength(), currentOrientation)) {
            System.out.println("Posicion valida para el barco");
        } else {
            System.out.println("Posicion invalida para el barco");
        }
    }

    private void checkGameReady() {
        int[] shipCounts = model.getShipCounts();
        int[] shipsPlaced = model.getShipsPlaced();
        boolean allShipsPlaced = true;
        for (int i = 0; i < shipCounts.length; i++) {
            if (shipsPlaced[i] < shipCounts[i]) {
                allShipsPlaced = false;
                break;
            }
        }

        if (allShipsPlaced) {
            System.out.println("Todos los barcos han sido colocados. El juego esta listo.");
            // Aquí podrías habilitar el botón de "Iniciar Batalla" o similar
        } else {
            int totalRemaining = 0;
            for (int i = 0; i < shipCounts.length; i++) {
                totalRemaining += (shipCounts[i] - shipsPlaced[i]);
            }
            System.out.println("Barcos restantes por colocar: " + totalRemaining);
        }
    }

    // Métodos públicos para acceso desde Main si es necesario
    public Board getModel() {
        return model;
    }

    public BoardView getView() {
        return view;
    }

    public boolean isGameReady() {
        int[] shipCounts = model.getShipCounts();
        int[] shipsPlaced = model.getShipsPlaced();
        for (int i = 0; i < shipCounts.length; i++) {
            if (shipsPlaced[i] < shipCounts[i]) {
                return false;
            }
        }
        return true;
    }
}
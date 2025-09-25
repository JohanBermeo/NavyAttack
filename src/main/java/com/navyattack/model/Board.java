package com.navyattack.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 10;

    private CellState[][] board;
    private List<Ship> ships;

    public Board() {
        board = new CellState[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = CellState.EMPTY;
            }
        }
        ships = new ArrayList<Ship>();
    }

    public boolean canPlaceShipAt(int row, int col, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            if (col + length > BOARD_SIZE) return false;
            for (int c = col; c < col + length; c++) {
                if (board[row][c] != CellState.EMPTY) return false;
            }
        } else {
            if (row + length > BOARD_SIZE) return false;
            for (int r = row; r < row + length; r++) {
                if (board[r][col] != CellState.EMPTY) return false;
            }
        }
        return true;
    }

    public void placeShip(Ship selectedShip, int row, int col, int length, Orientation orientation) {
        // Corregido: usar la orientación pasada como parámetro
        if (!canPlaceShipAt(row, col, length, orientation)) {
            throw new IllegalArgumentException("Cannot place ship at the specified location");
        }

        // Actualizar la posición del barco existente en lugar de crear uno nuevo
        selectedShip.setRow(row);
        selectedShip.setCol(col);
        selectedShip.setOrientation(orientation);
        selectedShip.setPlaced(true);

        // Calcular y establecer las posiciones del barco
        List<int[]> positions = new ArrayList<>();
        if (orientation == Orientation.HORIZONTAL) {
            for (int c = col; c < col + length; c++) {
                board[row][c] = CellState.SHIP;
                positions.add(new int[]{row, c});
            }
        } else {
            for (int r = row; r < row + length; r++) {
                board[r][col] = CellState.SHIP;
                positions.add(new int[]{r, col});
            }
        }

        selectedShip.setPositions(positions);
        ships.add(selectedShip);
    }

    public CellState getCellState(int row, int col) {
        return board[row][col];
    }

    public List<Ship> getShips() {
        return ships;
    }

    // Método para actualizar visualmente el tablero (para futuras implementaciones)
    public void updateBoard() {
        // Limpiar tablero
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == CellState.SHIP) {
                    board[i][j] = CellState.EMPTY;
                }
            }
        }

        // Redibujar todos los barcos
        for (Ship ship : ships) {
            if (ship.isPlaced()) {
                for (int[] pos : ship.getPositions()) {
                    board[pos[0]][pos[1]] = CellState.SHIP;
                }
            }
        }
    }
}
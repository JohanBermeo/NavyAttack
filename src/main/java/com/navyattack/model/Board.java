package com.navyattack.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 10;

    private CellState[][] board;
    private List<Ship> ships;
    private Orientation orientation;

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
        if (!canPlaceShipAt(row, col, length, Orientation.HORIZONTAL)) {
            throw new IllegalArgumentException("Cannot place ship at the specified location");
        }
        Ship ship = new Ship(row, col, length, orientation, false, new ArrayList<>(), 0, false);
        ships.add(ship);
        if (orientation == Orientation.HORIZONTAL) {
            for (int c = col; c < col + length; c++) {
                board[row][c] = CellState.SHIP;
            }
        } else {
            for (int r = row; r < row + length; r++) {
                board[r][col] = CellState.SHIP;
            }
        }
    }

    public CellState getCellState(int row, int col) {
        return board[row][col];
    }

    public void rotateCurrentShip() {

    }

    public void selectShipToDeploy(ShipType shipType) {

    }

    public void addListener() {

    }
}

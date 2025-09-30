package com.navyattack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ship {
    private final ShipType type;
    private final int length;
    private Orientation orientation;

    private Integer row;
    private Integer col;
    private List<int[]> positions;
    private int hits;

    public Ship(ShipType type) {
        this.type = type;
        this.length = type.getLength();
        this.orientation = Orientation.HORIZONTAL;
        this.hits = 0;
    }

    // ✓ El MODELO calcula sus propias posiciones
    public List<int[]> calculatePositions(int startRow, int startCol) {
        List<int[]> positions = new ArrayList<>();

        if (orientation == Orientation.HORIZONTAL) {
            for (int c = 0; c < length; c++) {
                positions.add(new int[]{startRow, startCol + c});
            }
        } else {
            for (int r = 0; r < length; r++) {
                positions.add(new int[]{startRow + r, startCol});
            }
        }

        return positions;
    }

    public void place(int row, int col) {
        this.row = row;
        this.col = col;
        this.positions = calculatePositions(row, col);
    }

    public void rotate() {
        orientation = (orientation == Orientation.HORIZONTAL)
                ? Orientation.VERTICAL
                : Orientation.HORIZONTAL;
    }

    public void registerHit() {
        hits++;
    }

    public boolean isSunk() {
        return hits >= length;
    }

    public boolean isPlaced() {
        return row != null && col != null;
    }

    // Getters
    public ShipType getType() { return type; }
    public int getLength() { return length; }
    public Orientation getOrientation() { return orientation; }
    public List<int[]> getPositions() {
        return isPlaced() ? Collections.unmodifiableList(positions) : Collections.emptyList();
    }
}
package com.navyattack.model;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private int row;
    private int col;
    private int length;
    private Orientation orientation;
    private boolean placed;
    private List<int[]> positions;
    private int hits;
    private boolean sunk;
    private boolean deployable;  // Corregido: era "debloyable"
    private boolean deploymentMode;

    public Ship(int row, int col, int length, Orientation orientation, boolean placed, List<int[]> positions, int hits, boolean sunk) {
        this.row = row;
        this.col = col;
        this.length = length;
        this.orientation = orientation; // Corregido: usar el parámetro
        this.placed = placed; // Corregido: usar el parámetro
        this.positions = positions != null ? positions : new ArrayList<>(); // Corregido: usar el parámetro
        this.hits = hits; // Corregido: usar el parámetro
        this.sunk = sunk; // Corregido: usar el parámetro
        this.deployable = true; // Por defecto deployable
        this.deploymentMode = false;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public List<int[]> getPositions() {
        return positions;
    }

    public void setPositions(List<int[]> positions) {
        this.positions = positions;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }

    public boolean isDeploymentMode() {
        return deploymentMode;
    }

    public void setDeploymentMode(boolean deploymentMode) {
        this.deploymentMode = deploymentMode;
    }

    public boolean isDeployable() {
        return deployable; // Corregido: era "debloyable"
    }

    public void setDeployable(boolean deployable) { // Corregido: nombre del método
        this.deployable = deployable;
    }
}
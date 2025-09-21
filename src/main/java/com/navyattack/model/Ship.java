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
    private boolean debloyable;
    private boolean deploymentMode;

    public Ship(int row, int col, int length, Orientation horizontal, boolean placed, List<int[]> positions, int hits, boolean sunk) {
        this.row = row;
        this.col = col;
        this.orientation = Orientation.HORIZONTAL;
        this.placed = false;
        this.positions = new ArrayList<>();
        this.hits = 0;
        this.sunk = false;
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
        return debloyable;
    }

    public void setDebloyable(boolean debloyable) {
        this.debloyable = debloyable;
    }

}

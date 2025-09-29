package com.navyattack.model;

public enum ShipType {
    CARRY(6), CRUISER(4), DESTROYER(3), SUBMARINE(2);

    private final int length;

    ShipType(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
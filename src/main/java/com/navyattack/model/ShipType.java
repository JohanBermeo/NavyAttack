package com.navyattack.model;

/**
 * Enumeración que representa los diferentes tipos de barcos disponibles en NavyAttack.
 * Cada tipo de barco tiene una longitud específica que determina cuántas celdas
 * ocupa en el tablero.
 * 
 * Según las reglas estándar del juego:
 * - 1 Portaviones (6 celdas)
 * - 2 Cruceros (4 celdas cada uno)
 * - 3 Destructores (3 celdas cada uno)
 * - 4 Submarinos (2 celdas cada uno)
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public enum ShipType {
    
    /**
     * Portaviones - El barco más grande.
     * Ocupa 6 celdas en el tablero.
     */
    CARRY(6),
    
    /**
     * Crucero - Barco de tamaño medio-grande.
     * Ocupa 4 celdas en el tablero.
     */
    CRUISER(4),
    
    /**
     * Destructor - Barco de tamaño medio.
     * Ocupa 3 celdas en el tablero.
     */
    DESTROYER(3),
    
    /**
     * Submarino - El barco más pequeño.
     * Ocupa 2 celdas en el tablero.
     */
    SUBMARINE(2);
    
    /**
     * Longitud del barco en número de celdas que ocupa.
     */
    private final int length;
    
    /**
     * Constructor del tipo de barco.
     * 
     * @param length Longitud del barco en celdas
     */
    ShipType(int length) {
        this.length = length;
    }
    
    /**
     * Obtiene la longitud del barco.
     * 
     * @return Número de celdas que ocupa el barco en el tablero
     */
    public int getLength() {
        return length;
    }
}
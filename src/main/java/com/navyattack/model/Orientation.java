package com.navyattack.model;

/**
 * Enumeración que representa la orientación de un barco en el tablero de juego.
 * Un barco puede estar colocado horizontalmente (de izquierda a derecha)
 * o verticalmente (de arriba hacia abajo).
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public enum Orientation {
    
    /**
     * Orientación horizontal.
     * El barco se extiende de izquierda a derecha (a lo largo de las columnas).
     */
    HORIZONTAL,
    
    /**
     * Orientación vertical.
     * El barco se extiende de arriba hacia abajo (a lo largo de las filas).
     */
    VERTICAL
}
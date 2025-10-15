package com.navyattack.model;

/**
 * Enumeración que representa los posibles estados de una celda en el tablero de juego.
 * Cada celda del tablero puede tener uno de estos cuatro estados durante el desarrollo
 * de una partida de NavyAttack.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public enum CellState {
    
    /**
     * Celda vacía sin ningún barco.
     * Estado inicial de todas las celdas del tablero.
     */
    EMPTY,
    
    /**
     * Celda ocupada por parte de un barco.
     * Indica que hay un barco en esta posición que aún no ha sido impactado.
     */
    SHIP,
    
    /**
     * Celda que fue atacada e impactó un barco.
     * Indica que había un barco en esta posición y fue golpeado exitosamente.
     */
    HIT,
    
    /**
     * Celda que fue atacada pero no había ningún barco.
     * Indica un ataque fallido a agua.
     */
    MISS
}
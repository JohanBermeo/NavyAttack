package com.navyattack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa un barco en el juego NavyAttack.
 * Cada barco tiene un tipo específico que determina su longitud, puede estar
 * orientado horizontal o verticalmente, y mantiene registro de los impactos recibidos.
 * 
 * El barco calcula sus propias posiciones en el tablero según su orientación
 * y coordenadas iniciales, siguiendo el principio de responsabilidad única.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class Ship {
    
    /**
     * Tipo de barco (Portaviones, Crucero, Destructor o Submarino).
     */
    private final ShipType type;
    
    /**
     * Longitud del barco en número de celdas que ocupa.
     */
    private final int length;
    
    /**
     * Orientación actual del barco (horizontal o vertical).
     */
    private Orientation orientation;
    
    /**
     * Fila inicial donde está colocado el barco.
     * Null si el barco no ha sido colocado aún.
     */
    private Integer row;
    
    /**
     * Columna inicial donde está colocado el barco.
     * Null si el barco no ha sido colocado aún.
     */
    private Integer col;
    
    /**
     * Lista de posiciones que ocupa el barco en el tablero.
     * Cada posición es un arreglo [fila, columna].
     */
    private List<int[]> positions;
    
    /**
     * Número de impactos recibidos por el barco.
     */
    private int hits;
    
    /**
     * Constructor del barco.
     * Inicializa el barco con un tipo específico, orientación horizontal por defecto
     * y sin impactos.
     * 
     * @param type Tipo de barco a crear
     */
    public Ship(ShipType type) {
        this.type = type;
        this.length = type.getLength();
        this.orientation = Orientation.HORIZONTAL;
        this.hits = 0;
    }
    
    /**
     * Calcula las posiciones que ocuparía el barco dado un punto inicial.
     * Las posiciones se calculan según la orientación actual del barco.
     * En orientación horizontal, se extiende a lo largo de las columnas.
     * En orientación vertical, se extiende a lo largo de las filas.
     * 
     * @param startRow Fila inicial
     * @param startCol Columna inicial
     * @return Lista de posiciones [fila, columna] que ocuparía el barco
     */
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
    
    /**
     * Coloca el barco en el tablero en una posición específica.
     * Establece las coordenadas iniciales y calcula todas las posiciones
     * que ocupa el barco según su orientación actual.
     * 
     * @param row Fila inicial donde se colocará el barco
     * @param col Columna inicial donde se colocará el barco
     */
    public void place(int row, int col) {
        this.row = row;
        this.col = col;
        this.positions = calculatePositions(row, col);
    }
    
    /**
     * Rota el barco 90 grados.
     * Cambia la orientación entre horizontal y vertical.
     * Si está horizontal, pasa a vertical y viceversa.
     */
    public void rotate() {
        orientation = (orientation == Orientation.HORIZONTAL)
                ? Orientation.VERTICAL
                : Orientation.HORIZONTAL;
    }
    
    /**
     * Registra un impacto en el barco.
     * Incrementa el contador de impactos recibidos.
     */
    public void registerHit() {
        hits++;
    }
    
    /**
     * Verifica si el barco ha sido hundido.
     * Un barco está hundido cuando ha recibido impactos en todas sus posiciones.
     * 
     * @return true si el barco está hundido, false en caso contrario
     */
    public boolean isSunk() {
        return hits >= length;
    }
    
    /**
     * Verifica si el barco ha sido colocado en el tablero.
     * 
     * @return true si el barco tiene coordenadas asignadas, false en caso contrario
     */
    public boolean isPlaced() {
        return row != null && col != null;
    }
    
    /**
     * Obtiene el tipo del barco.
     * 
     * @return Tipo del barco
     */
    public ShipType getType() {
        return type;
    }
    
    /**
     * Obtiene la longitud del barco.
     * 
     * @return Número de celdas que ocupa el barco
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Obtiene la orientación actual del barco.
     * 
     * @return Orientación (HORIZONTAL o VERTICAL)
     */
    public Orientation getOrientation() {
        return orientation;
    }
    
    /**
     * Obtiene las posiciones que ocupa el barco en el tablero.
     * Si el barco no ha sido colocado, retorna una lista vacía.
     * 
     * @return Lista inmutable de posiciones [fila, columna], o lista vacía si no está colocado
     */
    public List<int[]> getPositions() {
        return isPlaced() ? Collections.unmodifiableList(positions) : Collections.emptyList();
    }
}
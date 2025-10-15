package com.navyattack.controller;

import com.navyattack.model.Ship;

/**
 * Encapsula el estado temporal durante la fase de deployment.
 * Esta clase gestiona la información del barco seleccionado y su posición objetivo
 * durante el proceso de colocación en el tablero. No representa un concepto del
 * dominio del juego, sino del proceso de interacción durante el deployment.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class DeploymentState {
    
    /**
     * Barco actualmente seleccionado para colocar en el tablero.
     */
    private Ship selectedShip;
    
    /**
     * Fila objetivo donde se colocará el barco.
     * Null si no se ha seleccionado una posición.
     */
    private Integer targetRow;
    
    /**
     * Columna objetivo donde se colocará el barco.
     * Null si no se ha seleccionado una posición.
     */
    private Integer targetCol;
    
    /**
     * Selecciona un barco para colocar en el tablero.
     * Al seleccionar un nuevo barco, se reinician las coordenadas objetivo.
     * 
     * @param ship Barco a seleccionar para deployment
     */
    public void selectShip(Ship ship) {
        this.selectedShip = ship;
        this.targetRow = null;
        this.targetCol = null;
    }
    
    /**
     * Establece la posición objetivo donde se colocará el barco seleccionado.
     * 
     * @param row Fila objetivo en el tablero
     * @param col Columna objetivo en el tablero
     */
    public void setTargetPosition(int row, int col) {
        this.targetRow = row;
        this.targetCol = col;
    }
    
    /**
     * Rota el barco seleccionado 90 grados.
     * Cambia la orientación del barco entre horizontal y vertical.
     * Si no hay un barco seleccionado, no realiza ninguna acción.
     */
    public void rotateShip() {
        if (selectedShip != null) {
            selectedShip.rotate();
        }
    }
    
    /**
     * Reinicia el estado del deployment.
     * Elimina el barco seleccionado y las coordenadas objetivo.
     */
    public void reset() {
        this.selectedShip = null;
        this.targetRow = null;
        this.targetCol = null;
    }
    
    /**
     * Verifica si hay un barco seleccionado actualmente.
     * 
     * @return true si hay un barco seleccionado, false en caso contrario
     */
    public boolean hasSelectedShip() {
        return selectedShip != null;
    }
    
    /**
     * Verifica si se ha establecido una posición objetivo.
     * 
     * @return true si se han establecido tanto fila como columna objetivo, false en caso contrario
     */
    public boolean hasTargetPosition() {
        return targetRow != null && targetCol != null;
    }
    
    /**
     * Verifica si el estado está listo para colocar un barco.
     * Un estado está listo cuando hay un barco seleccionado y una posición objetivo definida.
     * 
     * @return true si hay un barco seleccionado y una posición objetivo, false en caso contrario
     */
    public boolean isReadyToPlace() {
        return hasSelectedShip() && hasTargetPosition();
    }
    
    /**
     * Obtiene el barco actualmente seleccionado.
     * 
     * @return Barco seleccionado, o null si no hay ninguno seleccionado
     */
    public Ship getSelectedShip() {
        return selectedShip;
    }
    
    /**
     * Obtiene la fila objetivo para colocar el barco.
     * 
     * @return Fila objetivo, o null si no se ha establecido
     */
    public Integer getRow() {
        return targetRow;
    }
    
    /**
     * Obtiene la columna objetivo para colocar el barco.
     * 
     * @return Columna objetivo, o null si no se ha establecido
     */
    public Integer getCol() {
        return targetCol;
    }
}
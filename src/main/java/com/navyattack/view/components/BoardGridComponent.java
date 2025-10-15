package com.navyattack.view.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Componente reutilizable que representa un tablero de juego en NavyAttack.
 * Proporciona una cuadrícula visual de celdas interactivas o no interactivas
 * que pueden mostrar barcos, ataques y diferentes estados del juego.
 * 
 * Este componente encapsula toda la lógica de visualización del tablero,
 * incluyendo la gestión de estilos, eventos de click y marcado de celdas.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class BoardGridComponent {

    /**
     * GridPane de JavaFX que contiene la cuadrícula del tablero.
     */
    private final GridPane gridPane;
    
    /**
     * Matriz bidimensional de botones que representan las celdas del tablero.
     */
    private final Button[][] cells;
    
    /**
     * Tamaño del tablero (normalmente 10x10).
     */
    private final int size;
    
    /**
     * Indica si el tablero es interactivo (responde a clicks del usuario).
     */
    private final boolean interactive;

    /**
     * Constructor del componente de tablero.
     * Inicializa la cuadrícula con el tamaño especificado y configura
     * si las celdas responderán a eventos de click.
     * 
     * @param size Tamaño del tablero (número de filas y columnas)
     * @param interactive true si las celdas deben responder a clicks, false para tableros de solo lectura
     */
    public BoardGridComponent(int size, boolean interactive) {
        this.size = size;
        this.interactive = interactive;
        this.gridPane = new GridPane();
        this.cells = new Button[size][size];

        initializeGrid();
    }

    /**
     * Inicializa la cuadrícula del tablero.
     * Crea todas las celdas y las agrega al GridPane con el estilo apropiado.
     */
    private void initializeGrid() {
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setStyle("-fx-background-color: #5872C9; -fx-padding: 10px;");

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button cell = createCell(row, col);
                cells[row][col] = cell;
                gridPane.add(cell, col, row);
            }
        }
    }

    /**
     * Crea una celda individual del tablero.
     * Configura el tamaño, estilo y datos asociados a la celda.
     * 
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return Button configurado como celda del tablero
     */
    private Button createCell(int row, int col) {
        Button cell = new Button();
        cell.setPrefSize(50, 50);
        cell.setStyle(getCellStyle());
        cell.setUserData(new int[]{row, col});

        if (!interactive) {
            cell.setDisable(true);
        }

        return cell;
    }

    /**
     * Obtiene el estilo CSS por defecto para una celda del tablero.
     * 
     * @return String con el estilo CSS de una celda vacía
     */
    private String getCellStyle() {
        return "-fx-background-color: white; " +
                "-fx-border-color: #000000; " +
                "-fx-border-width: 1px;";
    }

    /**
     * Obtiene el GridPane que contiene el tablero.
     * 
     * @return GridPane del tablero
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Establece el manejador de eventos para clicks en las celdas.
     * Solo funciona si el tablero es interactivo.
     *
     * @param handler EventHandler que se ejecutará al hacer click en una celda
     */
    public void setCellClickHandler(EventHandler<ActionEvent> handler) {
        if (!interactive) {
            System.out.println("WARNING: Trying to set click handler on non-interactive board");
            return;
        }

        System.out.println("Setting cell click handler on " + size + "x" + size + " board");

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button cell = cells[row][col];

                // Verificar que la celda tenga userData
                if (cell.getUserData() == null) {
                    System.err.println("WARNING: Cell [" + row + "," + col + "] has no userData");
                    cell.setUserData(new int[]{row, col});
                }

                // Establecer el handler
                cell.setOnAction(handler);
            }
        }
    }

    /**
     * Resalta un conjunto de celdas con un color específico.
     * Limpia los resaltados previos antes de aplicar el nuevo.
     * Utilizado para previsualizar la colocación de barcos.
     * 
     * @param positions Lista de posiciones [fila, columna] a resaltar
     * @param color Código de color hexadecimal para el resaltado
     */
    public void highlightCells(java.util.List<int[]> positions, String color) {
        clearHighlights();
        String style = String.format("-fx-background-color: %s; -fx-border-color: #333; -fx-border-width: 2px;", color);

        for (int[] pos : positions) {
            if (isValidPosition(pos[0], pos[1])) {
                cells[pos[0]][pos[1]].setStyle(style);
            }
        }
    }

    /**
     * Muestra las celdas ocupadas por barcos con un estilo específico.
     * Utilizado para visualizar los barcos colocados en el tablero.
     * 
     * @param positions Lista de posiciones [fila, columna] ocupadas por barcos
     */
    public void showShipCells(java.util.List<int[]> positions) {
        String shipStyle = "-fx-background-color: #666; -fx-border-color: #333; -fx-border-width: 2px;";

        for (int[] pos : positions) {
            if (isValidPosition(pos[0], pos[1])) {
                cells[pos[0]][pos[1]].setStyle(shipStyle);
            }
        }
    }

    /**
     * Marca una celda como impactada (HIT).
     * Aplica estilo visual y texto para indicar un impacto exitoso.
     * 
     * @param row Fila de la celda impactada
     * @param col Columna de la celda impactada
     */
    public void markHit(int row, int col) {
        if (isValidPosition(row, col)) {
            cells[row][col].setStyle("-fx-background-color: #ff4444; -fx-border-color: #333;");
            cells[row][col].setText("X");
        }
    }

    /**
     * Marca una celda como fallida (MISS).
     * Aplica estilo visual y texto para indicar un ataque al agua.
     * 
     * @param row Fila de la celda fallada
     * @param col Columna de la celda fallada
     */
    public void markMiss(int row, int col) {
        if (isValidPosition(row, col)) {
            cells[row][col].setStyle("-fx-background-color: #4444ff; -fx-border-color: #333;");
            cells[row][col].setText("○");
        }
    }

    /**
     * Limpia los resaltados temporales del tablero.
     * Elimina colores de previsualización pero mantiene barcos colocados.
     */
    public void clearHighlights() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                String style = cells[r][c].getStyle();
                if (style.contains("#ffeb3b") || style.contains("#90EE90") || style.contains("#FF6B6B")) {
                    // Solo limpiar highlights, no barcos colocados
                    if (!style.contains("#666")) {
                        cells[r][c].setStyle(getCellStyle());
                    }
                }
            }
        }
    }

    /**
     * Deshabilita todas las celdas del tablero.
     * Previene la interacción del usuario con el tablero.
     */
    public void disableAllCells() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].setDisable(true);
            }
        }
    }

    /**
     * Habilita todas las celdas del tablero.
     * Solo funciona si el tablero fue configurado como interactivo.
     */
    public void enableAllCells() {
        if (!interactive) return;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].setDisable(false);
            }
        }
    }

    /**
     * Reinicia todas las celdas a su estado inicial.
     * Elimina texto y restaura el estilo por defecto.
     */
    public void reset() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].setText("");
                cells[row][col].setStyle(getCellStyle());
            }
        }
    }

    /**
     * Limpia completamente el tablero.
     * Utilizado en el intercambio de displays entre jugadores en modo PVP.
     */
    public void clearAll() {
        reset();
        clearHighlights();
    }

    /**
     * Verifica si una posición está dentro de los límites del tablero.
     * 
     * @param row Fila a verificar
     * @param col Columna a verificar
     * @return true si la posición es válida, false en caso contrario
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}
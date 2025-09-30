package com.navyattack.view.components;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * Componente reutilizable que representa un tablero de juego.
 * NO es una vista completa, es un COMPONENTE que se puede incluir en otras vistas.
 */
public class BoardGridComponent {

    private final GridPane gridPane;
    private final Button[][] cells;
    private final int size;
    private final boolean interactive;

    /**
     * @param size Tamaño del tablero (normalmente 10)
     * @param interactive Si true, las celdas responden a clicks
     */
    public BoardGridComponent(int size, boolean interactive) {
        this.size = size;
        this.interactive = interactive;
        this.gridPane = new GridPane();
        this.cells = new Button[size][size];

        initializeGrid();
    }

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

    private Button createCell(int row, int col) {
        Button cell = new Button();
        cell.setPrefSize(50, 50); // Más pequeño para tableros lado a lado
        cell.setStyle(getCellStyle());
        cell.setUserData(new int[]{row, col});

        if (!interactive) {
            cell.setDisable(true);
        }

        return cell;
    }

    private String getCellStyle() {
        return "-fx-background-color: white; " +
                "-fx-border-color: #000000; " +
                "-fx-border-width: 1px;";
    }

    // ===== MÉTODOS PÚBLICOS =====

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setCellClickHandler(EventHandler<ActionEvent> handler) {
        if (!interactive) return;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].setOnAction(handler);
            }
        }
    }

    public void highlightCells(java.util.List<int[]> positions, String color) {
        clearHighlights();
        String style = String.format("-fx-background-color: %s; -fx-border-color: #333; -fx-border-width: 2px;", color);

        for (int[] pos : positions) {
            if (isValidPosition(pos[0], pos[1])) {
                cells[pos[0]][pos[1]].setStyle(style);
            }
        }
    }

    public void showShipCells(java.util.List<int[]> positions) {
        String shipStyle = "-fx-background-color: #666; -fx-border-color: #333; -fx-border-width: 2px;";

        for (int[] pos : positions) {
            if (isValidPosition(pos[0], pos[1])) {
                cells[pos[0]][pos[1]].setStyle(shipStyle);
            }
        }
    }

    public void markHit(int row, int col) {
        if (isValidPosition(row, col)) {
            cells[row][col].setStyle("-fx-background-color: #ff4444; -fx-border-color: #333;");
            cells[row][col].setText("X");
        }
    }

    public void markMiss(int row, int col) {
        if (isValidPosition(row, col)) {
            cells[row][col].setStyle("-fx-background-color: #4444ff; -fx-border-color: #333;");
            cells[row][col].setText("○");
        }
    }

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

    public void disableAllCells() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].setDisable(true);
            }
        }
    }

    public void enableAllCells() {
        if (!interactive) return;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].setDisable(false);
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}
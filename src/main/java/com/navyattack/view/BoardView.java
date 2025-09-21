package com.navyattack.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Objects;

/**
 * Vista del menú principal de NavyAttack
 * Incluye diferentes métodos para personalizar el fondo
 */
public class BoardView {

    private Scene scene;
    private Button btnDeployCarry;
    private Button btnDeployCruiser;
    private Button getBtnDeployDestroyer;
    private Button btnDeploySubmarine;
    private Button btnRotateShip;
    private Button btnPlaceShip;
    private VBox boardLayout;
    private Button[][] boardCells = new Button[10][10];
    private GridPane boardGrid;

    public BoardView() {
        // Inicializar componentes
        btnDeployCarry = new Button("Añadir Portaaviones");
        btnDeployCruiser = new Button("Añadir Crucero");
        getBtnDeployDestroyer = new Button("Añadir Destructor");
        btnDeploySubmarine = new Button("Añadir Submarino");
        btnRotateShip = new Button("Rotar Barco [R]");
        btnPlaceShip = new Button("Colocar Barco [Enter]");

        // Configurar layout principal
        boardGrid = new GridPane();
        boardGrid.setGridLinesVisible(true);
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        boardGrid.setPadding(new Insets(10));

        //Creacion de celdas
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Button cell = new Button();
                cell.setPrefSize(40, 40);
                cell.getStyleClass().add("board-cell"); // Solo CSS externo
                cell.setUserData(new int[]{row, col}); // Asignar posición
                boardCells[row][col] = cell;
                boardGrid.add(cell, col, row);
            }
        }

        //Panel de botones
        VBox controlsPanel = new VBox(15);
        controlsPanel.setPadding(new Insets(20));
        controlsPanel.setAlignment(Pos.CENTER);

        //Imagenes
        Image carryImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("./sprites/Portaviones.svg")));
        ImageView carryImageView = new ImageView(carryImage);
        carryImageView.setFitWidth(80);
        carryImageView.setPreserveRatio(true);

        Image cruiserImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("./sprites/Cruceros.svg")));
        ImageView cruiserImageView = new ImageView(cruiserImage);
        cruiserImageView.setFitWidth(80);
        cruiserImageView.setPreserveRatio(true);

        Image destroyerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("./sprites/Destructores.svg")));
        ImageView destroyerImageView = new ImageView(destroyerImage);
        destroyerImageView.setFitWidth(80);
        destroyerImageView.setPreserveRatio(true);

        Image submarineImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("./sprites/Submarino.svg")));
        ImageView submarineImageView = new ImageView(submarineImage);
        submarineImageView.setFitWidth(80);
        submarineImageView.setPreserveRatio(true);

        //Añadir botones e imagenes al panel
        controlsPanel.getChildren().addAll(
                carryImageView, btnDeployCarry,
                cruiserImageView, btnDeployCruiser,
                destroyerImageView, getBtnDeployDestroyer,
                submarineImageView, btnDeploySubmarine,
                btnRotateShip, btnPlaceShip
        );

        // Layout principal
        HBox root = new HBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(boardGrid, controlsPanel);

        // Crear escena
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/navyattack/view/styles/menu-styles.css")).toExternalForm());
    }

    /**
     * Asigna un EventHandler a todas las celdas del tablero
     */
    public void setCellClickListener(EventHandler<ActionEvent> handler) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                boardCells[row][col].setOnAction(handler);
            }
        }
    }

    /**
     * Resalta una celda del tablero usando una clase CSS
     */
    public void highlightCell(int row, int col) {
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                boardCells[r][c].getStyleClass().remove("highlighted-cell");
            }
        }
        boardCells[row][col].getStyleClass().add("highlighted-cell");
    }

    public void setBtnDeployCarryListener(EventHandler<ActionEvent> handler) {
        btnDeployCarry.setOnAction(handler);
    }

    public void setBtnDeployCruiserListener(EventHandler<ActionEvent> handler) {
        btnDeployCruiser.setOnAction(handler);
    }

    public void setBtnDeployDestroyerListener(EventHandler<ActionEvent> handler) {
        getBtnDeployDestroyer.setOnAction(handler);
    }

    public void setBtnDeploySubmarineListener(EventHandler<ActionEvent> handler) {
        btnDeploySubmarine.setOnAction(handler);
    }

    public void setBtnRotateShipListener(EventHandler<ActionEvent> handler) {
        btnRotateShip.setOnAction(handler);
    }

    public void setBtnPlaceShipListener(EventHandler<ActionEvent> handler) {
        btnPlaceShip.setOnAction(handler);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getCellButton(int row, int col) {
        return boardCells[row][col];
    }

    // Ejemplo de cómo conectar los handlers (esto normalmente se hace en el controlador)
    public void connectButtonHandlers() {
        setBtnDeployCarryListener(null); // Asignar en el controlador
        setBtnDeployCruiserListener(null);
        setBtnDeployDestroyerListener(null);
        setBtnDeploySubmarineListener(null);
        setBtnRotateShipListener(null);
        setBtnPlaceShipListener(null);
    }
}
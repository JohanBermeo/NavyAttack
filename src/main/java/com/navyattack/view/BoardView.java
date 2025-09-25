package com.navyattack.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Vista del tablero de NavyAttack
 * Rediseñada según mockup proporcionado
 */
public class BoardView {

    private Scene scene;
    private Button btnDeployCarry;
    private Button btnDeployCruiser;
    private Button btnDeployDestroyer;
    private Button btnDeploySubmarine;
    private Button btnRotateShip;
    private Button btnPlaceShip;
    private Button[][] boardCells = new Button[10][10];
    private GridPane boardGrid;
    private Label[] shipCountLabels = new Label[4]; // Para mostrar cantidad disponible

    public BoardView() {
        // Crear el layout principal
        HBox mainLayout = new HBox();
        mainLayout.setSpacing(0);
        mainLayout.setPrefSize(1080, 720);

        // Panel izquierdo - Tablero
        VBox leftPanel = createBoardPanel();
        leftPanel.setPrefWidth(720);

        // Panel derecho - Controles
        VBox rightPanel = createControlPanel();
        rightPanel.setPrefWidth(360);

        mainLayout.getChildren().addAll(leftPanel, rightPanel);

        // Crear escena
        scene = new Scene(mainLayout, 1080, 720);

        // Aplicar estilos
        applyStyles();
    }

    private VBox createBoardPanel() {
        VBox boardPanel = new VBox();
        boardPanel.setAlignment(Pos.CENTER);
        boardPanel.setStyle("-fx-background-color: #5872C9; -fx-padding: 20px;");

        // Crear grid del tablero
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(0);
        boardGrid.setVgap(0);
        boardGrid.setStyle("-fx-background-color: #5872C9; -fx-padding: 10px;");

        // Crear celdas del tablero
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Button cell = new Button();
                cell.setPrefSize(60, 60);
                cell.setStyle("-fx-background-color: white; -fx-border-color: #000000; -fx-border-width: 1px;");
                cell.setUserData(new int[]{row, col});
                boardCells[row][col] = cell;
                boardGrid.add(cell, col, row);
            }
        }

        boardPanel.getChildren().addAll(boardGrid);
        return boardPanel;
    }

    private VBox createControlPanel() {
        VBox controlPanel = new VBox();
        controlPanel.setAlignment(Pos.TOP_CENTER);
        controlPanel.setSpacing(15); // Reducido para dar más espacio a las imágenes
        controlPanel.setStyle("-fx-background-color: #5872C9; -fx-padding: 20px;");

        // Crear cada sección de barco con más espacio para imágenes
        VBox carrySection = createShipSection("Añadir Portaaviones", "Portaviones.png", 0);
        VBox cruiserSection = createShipSection("Añadir Crucero", "Cruceros.png", 1);
        VBox destroyerSection = createShipSection("Añadir Destructor", "Destructores.png", 2);
        VBox submarineSection = createShipSection("Añadir Submarino", "Submarino.png", 3);

        // Botones de acción
        HBox actionButtons = createActionButtons();

        controlPanel.getChildren().addAll(
                carrySection,
                cruiserSection,
                destroyerSection,
                submarineSection,
                actionButtons
        );

        return controlPanel;
    }

    private VBox createShipSection(String buttonText, String imagePath, int shipIndex) {
        VBox section = new VBox();
        section.setAlignment(Pos.CENTER);
        section.setSpacing(8);

        // Configurar dimensiones específicas para cada tipo de barco
        double width, height;
        switch(shipIndex) {
            case 0: // Portaaviones
                width = 245;
                height = 92;
                break;
            case 1: // Cruceros
                width = 169;
                height = 53;
                break;
            case 2: // Destructores
                width = 112;
                height = 40;
                break;
            case 3: // Submarinos
                width = 56;
                height = 33;
                break;
            default:
                width = 100;
                height = 50;
        }

        // Contenedor para la imagen con altura específica
        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefHeight(height);
        imageContainer.setMaxHeight(height);

        // Imagen del barco
        ImageView shipImage = createShipImage(imagePath, width, height);
        imageContainer.getChildren().add(shipImage);

        // Contenedor horizontal para botón y contador
        HBox buttonRow = new HBox();
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setSpacing(15);

        // Botón
        Button shipButton = new Button(buttonText);
        shipButton.setStyle(getShipButtonStyle());

        // Asignar referencia según el tipo
        switch (shipIndex) {
            case 0 -> btnDeployCarry = shipButton;
            case 1 -> btnDeployCruiser = shipButton;
            case 2 -> btnDeployDestroyer = shipButton;
            case 3 -> btnDeploySubmarine = shipButton;
        }

        // Contador (círculo con número)
        Label countLabel = new Label("1");
        countLabel.setStyle(getCountLabelStyle());
        shipCountLabels[shipIndex] = countLabel;

        buttonRow.getChildren().addAll(shipButton, countLabel);
        section.getChildren().addAll(imageContainer, buttonRow);

        return section;
    }

    private ImageView createShipImage(String imagePath, double width, double height) {
        System.out.println("=== Creando imagen: " + imagePath + " ===");

        // Primero intentar cargar la imagen
        Image image = loadImageFromResources(imagePath);

        if (image != null && !image.isError() && image.getHeight() > 0) {
            System.out.println("Imagen cargada correctamente: " + image.getWidth() + "x" + image.getHeight());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setSmooth(true);
            return imageView;
        } else {
            System.out.println("Creando placeholder para: " + imagePath);
            return createColoredPlaceholder(width, height, imagePath);
        }
    }

    private Image loadImageFromResources(String imagePath) {
        String[] possiblePaths = {
                "/images/sprites/" + imagePath,
                "/sprites/" + imagePath,
                "/" + imagePath,
                "images/sprites/" + imagePath,
                "sprites/" + imagePath,
                imagePath
        };

        for (String path : possiblePaths) {
            try {
                System.out.println("Intentando: " + path);
                var stream = getClass().getResourceAsStream(path);
                if (stream != null) {
                    Image img = new Image(stream);
                    if (!img.isError() && img.getHeight() > 0) {
                        System.out.println("✓ Éxito: " + path);
                        return img;
                    }
                    stream.close();
                }
            } catch (Exception e) {
                System.out.println("✗ Error en " + path + ": " + e.getMessage());
            }
        }
        return null;
    }

    private ImageView createColoredPlaceholder(double width, double height, String fileName) {
        // Colores diferentes según el tipo de barco
        String color = switch (fileName.toLowerCase()) {
            case "portaviones.png" -> "#8B4513"; // Marrón
            case "cruceros.png" -> "#708090";    // Gris pizarra
            case "destructores.png" -> "#2F4F4F"; // Verde oscuro
            case "submarino.png" -> "#191970";   // Azul medianoche
            default -> "#696969"; // Gris oscuro
        };

        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(width, height);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

        // Dibujar forma de barco
        gc.setFill(javafx.scene.paint.Color.web(color));
        gc.fillRoundRect(0, height * 0.2, width, height * 0.6, 10, 10);

        // Detalles del barco
        gc.setFill(javafx.scene.paint.Color.web("#333333"));
        gc.fillRoundRect(width * 0.1, height * 0.3, width * 0.8, height * 0.1, 5, 5);
        gc.fillRoundRect(width * 0.1, height * 0.6, width * 0.8, height * 0.1, 5, 5);

        // Convertir a imagen
        javafx.scene.image.WritableImage placeholderImage = new javafx.scene.image.WritableImage((int)width, (int)height);
        canvas.snapshot(null, placeholderImage);

        ImageView placeholder = new ImageView(placeholderImage);
        placeholder.setFitWidth(width);
        placeholder.setFitHeight(height);

        System.out.println("Placeholder creado para: " + fileName + " (Dimensiones: " + width + "x" + height + ")");
        return placeholder;
    }

    private HBox createActionButtons() {
        HBox actionBox = new HBox();
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setSpacing(20);
        actionBox.setPadding(new Insets(20, 0, 0, 0));

        btnRotateShip = new Button("Rotar [R]");
        btnRotateShip.setStyle(getActionButtonStyle());

        btnPlaceShip = new Button("Posicionar [Enter]");
        btnPlaceShip.setStyle(getActionButtonStyle());

        actionBox.getChildren().addAll(btnRotateShip, btnPlaceShip);
        return actionBox;
    }

    private String getShipButtonStyle() {
        return """
            -fx-background-color: white;
            -fx-text-fill: #0F1157;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-background-radius: 20px;
            -fx-border-radius: 20px;
            -fx-padding: 8px 16px;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 1, 1);
            """;
    }

    private String getActionButtonStyle() {
        return """
            -fx-background-color: #2c2c2c;
            -fx-text-fill: white;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-background-radius: 15px;
            -fx-border-radius: 15px;
            -fx-padding: 10px 20px;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 3, 0, 1, 1);
            """;
    }

    private String getCountLabelStyle() {
        return """
            -fx-background-color: transparent;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 15px;
            -fx-border-radius: 15px;
            -fx-min-width: 20px;
            -fx-min-height: 20px;
            -fx-alignment: center;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 1, 1);
            """;
    }

    private void applyStyles() {
        setupHoverEffects();
    }

    private void setupHoverEffects() {
        addHoverEffect(btnDeployCarry);
        addHoverEffect(btnDeployCruiser);
        addHoverEffect(btnDeployDestroyer);
        addHoverEffect(btnDeploySubmarine);
        addHoverEffect(btnRotateShip);
        addHoverEffect(btnPlaceShip);
    }

    private void addHoverEffect(Button button) {
        String originalStyle = button.getStyle();
        button.setOnMouseEntered(e -> button.setStyle(originalStyle + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        button.setOnMouseExited(e -> button.setStyle(originalStyle));
    }

    // Método para debug - puedes llamarlo desde el constructor temporalmente
    public void debugImagePaths() {
        System.out.println("=== DEBUG DE RUTAS DE IMÁGENES ===");
        String[] imageNames = {"Portaviones.png", "Cruceros.png", "Destructores.png", "Submarino.png"};

        for (String imageName : imageNames) {
            System.out.println("Verificando: " + imageName);
            var url = getClass().getResource("/images/sprites/" + imageName);
            if (url != null) {
                System.out.println("  ✓ Encontrado en: " + url);
            } else {
                System.out.println("  ✗ NO encontrado en /images/sprites/");
                // Intentar otras rutas
                url = getClass().getResource("/" + imageName);
                if (url != null) {
                    System.out.println("  ✓ Encontrado en root: " + url);
                } else {
                    System.out.println("  ✗ NO encontrado en ninguna ubicación");
                }
            }
        }
        System.out.println("=== FIN DEBUG ===");
    }

    // Métodos públicos para la funcionalidad
    public void setCellClickListener(EventHandler<ActionEvent> handler) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                boardCells[row][col].setOnAction(handler);
            }
        }
    }

    public void highlightCell(int row, int col) {
        // Resetear todas las celdas
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (boardCells[r][c].getStyle().contains("yellow")) {
                    boardCells[r][c].setStyle("-fx-background-color: white; -fx-border-color: #000000; -fx-border-width: 1px;");
                }
            }
        }
        // Resaltar celda seleccionada
        boardCells[row][col].setStyle("-fx-background-color: #ffeb3b; -fx-border-color: #333; -fx-border-width: 2px;");
    }

    public void showShipOnBoard(int row, int col, int length, com.navyattack.model.Orientation orientation) {
        String shipStyle = "-fx-background-color: #666; -fx-border-color: #333; -fx-border-width: 2px;";

        if (orientation == com.navyattack.model.Orientation.HORIZONTAL) {
            for (int c = col; c < col + length && c < 10; c++) {
                boardCells[row][c].setStyle(shipStyle);
            }
        } else {
            for (int r = row; r < row + length && r < 10; r++) {
                boardCells[r][col].setStyle(shipStyle);
            }
        }
    }

    public void updateShipCount(int shipType, int count) {
        if (shipType >= 0 && shipType < shipCountLabels.length) {
            shipCountLabels[shipType].setText(String.valueOf(count));
        }
    }

    // Getters para los botones
    public void setBtnDeployCarryListener(EventHandler<ActionEvent> handler) {
        btnDeployCarry.setOnAction(handler);
    }

    public void setBtnDeployCruiserListener(EventHandler<ActionEvent> handler) {
        btnDeployCruiser.setOnAction(handler);
    }

    public void setBtnDeployDestroyerListener(EventHandler<ActionEvent> handler) {
        btnDeployDestroyer.setOnAction(handler);
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
}

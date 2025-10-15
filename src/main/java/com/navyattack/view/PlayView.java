package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.DropShadow;
import javafx.application.Application;

import com.navyattack.controller.MenuController;
import com.navyattack.controller.NavigationController;

/**
 * Clase que representa la vista de selección de modo de juego en NavyAttack.
 * Permite al usuario elegir entre los modos "Player vs CPU" y "Player vs Player",
 * con un diseño visual adaptado al número de usuarios registrados.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class PlayView extends Application implements IView {
    
    /** Escena principal de la vista. */
    private Scene scene;
    /** Controlador encargado de la lógica del menú principal. */
    private MenuController menuController;
    /** Controlador encargado de la navegación entre vistas. */
    private NavigationController navigationController;
    
    /**
     * Constructor que inicializa la vista con sus controladores.
     * 
     * @param menuController controlador del menú principal.
     * @param navigationController controlador de navegación entre vistas.
     */
    public PlayView(MenuController menuController, NavigationController navigationController) {
        this.menuController = menuController;
        this.navigationController = navigationController;
    }
    
    /**
     * Inicializa la ventana principal de selección de modo de juego.
     * Configura los botones interactivos y el estilo de la escena.
     *
     * @param primaryStage el {@link Stage} principal donde se mostrará la vista.
     */
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(40);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #5872C9;");
        
        Label title = new Label("SELECT THE GAME MODE");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#FFFFFF"));
        
        VBox cardsContainer = new VBox(30);
        cardsContainer.setAlignment(Pos.CENTER);
        
        StackPane cardPlayerVsCPU = createCard(
            "Player vs CPU",
            "🤖",
            () -> navigationController.navigateToDeployment("PVC")
        );
        cardPlayerVsCPU.setStyle(
            "-fx-background-color: #C95858;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #FFFFFF;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );      
        cardPlayerVsCPU.setOnMouseExited(e -> {
            cardPlayerVsCPU.setStyle(
                "-fx-background-color: #C95858;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #FFFFFF;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;"
            );
            cardPlayerVsCPU.setScaleX(1.0);
            cardPlayerVsCPU.setScaleY(1.0);
        });  

        StackPane cardPlayerVsPlayer;

        // Configura el modo PVP dependiendo del número de usuarios registrados
        if (!menuController.hasSingleUser()) {
            cardPlayerVsPlayer = createCard(
                "Player vs Player",
                "👥",
                () -> navigationController.navigateToDeployment("PVP")
            );

            cardPlayerVsPlayer.setStyle(
                "-fx-background-color: linear-gradient(to right, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);" +
                "-fx-border-color: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;"
            );

            cardPlayerVsPlayer.setOnMouseEntered(e -> {
                cardPlayerVsPlayer.setScaleX(1.05);
                cardPlayerVsPlayer.setScaleY(1.05);
            });

            cardPlayerVsPlayer.setOnMouseExited(e -> {
                cardPlayerVsPlayer.setStyle(
                    "-fx-background-color: linear-gradient(to right, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);" +
                    "-fx-border-color: #FFFFFF;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 10;"
                );
                cardPlayerVsPlayer.setScaleX(1.0);
                cardPlayerVsPlayer.setScaleY(1.0);
            });

        } else {
            // Modo deshabilitado si solo hay un usuario
            cardPlayerVsPlayer = createCard("Player vs Player", "👥", () -> {});
            cardPlayerVsPlayer.setStyle(
                "-fx-background-color: #808080;" +
                "-fx-border-color: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-opacity: 0.5;"
            );
            cardPlayerVsPlayer.setCursor(Cursor.DEFAULT);
        }
        
        cardsContainer.getChildren().addAll(cardPlayerVsCPU, cardPlayerVsPlayer);       
        HBox bottomPanel = createBottomPanel();
        
        root.getChildren().addAll(title, cardsContainer, bottomPanel);
        
        scene = new Scene(root, 350, 450);
        primaryStage.setTitle("NavyAttack - Play");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    /**
     * Crea una tarjeta visual interactiva para seleccionar el modo de juego.
     *
     * @param text texto que describe el modo.
     * @param emoji ícono visual representativo.
     * @param action acción a ejecutar al hacer clic.
     * @return un {@link StackPane} configurado como tarjeta.
     */
    private StackPane createCard(String text, String emoji, Runnable action) {
        StackPane card = new StackPane();
        card.setPrefSize(200, 75);
        card.setCursor(Cursor.HAND);
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(250, 250, 250, 0.1));
        shadow.setRadius(10);
        card.setEffect(shadow);
        
        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);
        
        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font("Arial", 24));
        emojiLabel.setTextFill(Color.web("#FFFFFF"));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        textLabel.setTextFill(Color.web("#FFFFFF"));
        
        content.getChildren().addAll(emojiLabel, textLabel);
        card.getChildren().add(content);
        
        card.setOnMouseEntered(e -> {
            card.setScaleX(1.05);
            card.setScaleY(1.05);
        });

        card.setOnMouseClicked(e -> action.run());
        
        return card;
    }
    
    /**
     * Crea el panel inferior con el botón de retorno al menú principal.
     *
     * @return un {@link HBox} que contiene el botón de retorno.
     */
    private HBox createBottomPanel() { 
        HBox bottomPanel = new HBox(); 
        bottomPanel.setAlignment(Pos.CENTER); 
        bottomPanel.setPadding(new Insets(0, 0, 15, 0)); 

        Button backButton = new Button("Return"); 
        backButton.setPrefWidth(150); 
        UtilsMenuView.styleButton(backButton, "black", "#333333", "white", "5px 0 5px 0");
        backButton.setOnAction(e -> navigationController.navigateToView("menu")); 

        bottomPanel.getChildren().add(backButton); 
        return bottomPanel; 
    }

    /**
     * Retorna la escena actual asociada a la vista.
     *
     * @return la {@link Scene} principal de la vista.
     */
    @Override
    public Scene getScene() { 
        return scene; 
    }
}

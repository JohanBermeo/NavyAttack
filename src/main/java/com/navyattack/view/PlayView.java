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

public class PlayView extends Application implements IView {
    
    private Scene scene;
    private MenuController menuController;
    private NavigationController navigationController;
    
    public PlayView(MenuController menuController, NavigationController navigationController) {
        this.menuController = menuController;
        this.navigationController = navigationController;
    }
    
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
            () -> {
                navigationController.navigateToDeployment("PVC");
            }
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

        if (!menuController.hasSingleUser()) {
            // Caso: HAY DOS USUARIOS - Habilitar PVP
            cardPlayerVsPlayer = createCard(
                    "Player vs Player",
                    "👥",
                    () -> {
                        System.out.println("Iniciando modo Player vs Player");
                        navigationController.navigateToDeployment("PVP");
                    }
            );

            // Estilo activo con gradiente de dos colores
            cardPlayerVsPlayer.setStyle(
                    "-fx-background-color: linear-gradient(to right, #5872C9 0%, #5872C9 50%, #C95858 50%, #C95858 100%);" +
                            "-fx-border-color: #FFFFFF;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 10;"
            );

            // Efectos hover
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
            // Caso: SOLO UN USUARIO - Deshabilitar PVP
            cardPlayerVsPlayer = createCard(
                    "Player vs Player",
                    "👥",
                    () -> {
                        // No hacer nada (card deshabilitada)
                    }
            );

            // Estilo deshabilitado (gris y semi-transparente)
            cardPlayerVsPlayer.setStyle(
                    "-fx-background-color: #808080;" +
                            "-fx-border-color: #FFFFFF;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 10;" +
                            "-fx-opacity: 0.5;"
            );

            // Deshabilitar interacciones
            cardPlayerVsPlayer.setCursor(Cursor.DEFAULT);
            cardPlayerVsPlayer.setOnMouseEntered(null);
            cardPlayerVsPlayer.setOnMouseExited(null);
            cardPlayerVsPlayer.setOnMouseClicked(null);
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
    
    private HBox createBottomPanel() { 
        HBox bottomPanel = new HBox(); 
        bottomPanel.setAlignment(Pos.CENTER); 
        bottomPanel.setPadding(new Insets(0, 0, 15, 0)); 

        Button backButton = new Button("Return"); 
        backButton.setPrefWidth(150); 
        UtilsMenuView.styleButton(backButton, "black", "#333333", "white", "5px 0 5px 0");
        backButton.setOnAction(e -> {
            navigationController.navigateToView("menu");
        }); 

        bottomPanel.getChildren().add(backButton); 
        return bottomPanel; 
    }

    @Override
    public Scene getScene() { 
        return scene; 
    }
}
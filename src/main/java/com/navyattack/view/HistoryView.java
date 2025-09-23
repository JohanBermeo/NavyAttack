package com.navyattack.view; 
 
import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.geometry.Pos; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.geometry.Insets; 
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.application.Application; 

import com.navyattack.model.User; 
import com.navyattack.model.History; 
import com.navyattack.controller.MenuController;

public class HistoryView extends Application { 
     
    private Scene scene; 
    private User currentUser; 
    private MenuController controller; 
    private ListView<History> historyListView; 
     
    public HistoryView(MenuController controller, User user) {
    	this.controller = controller; 
        this.currentUser = user;
    }

    @Override 
	public void start(Stage primaryStage) { 
        primaryStage.setTitle("NavyAttack - History"); 
        
        BorderPane root = new BorderPane(); 
        root.setPadding(new Insets(15)); 
        
        VBox topPanel = createTopPanel(); 
        root.setTop(topPanel); 
        
        VBox centerPanel = createCenterPanel(); 
        root.setCenter(centerPanel);          
        
        HBox bottomPanel = createBottomPanel(); 
        root.setBottom(bottomPanel);          
        
        scene = new Scene(root, 500, 550); 
        scene.getRoot().setStyle("-fx-background-color: #5872C9;"); 
        primaryStage.setScene(scene);  
        primaryStage.show(); 
        loadHistories();
	}
     
    private VBox createTopPanel() { 
        VBox topPanel = new VBox(); 
        topPanel.setAlignment(Pos.CENTER); 
	    topPanel.setPadding(new Insets(0, 0, 15, 0)); 
         
        Label titleLabel = new Label("History - " + (currentUser != null ? currentUser.getUsername() : "N/A")); 
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");                   

        topPanel.getChildren().add(titleLabel); 
        return topPanel; 
    } 
     
    private VBox createCenterPanel() { 
        VBox centerPanel = new VBox(); 
        centerPanel.setAlignment(Pos.TOP_CENTER);

        StackPane historyContainer = new StackPane();
        historyContainer.setPrefWidth(450);
        historyContainer.setPrefHeight(400);

        historyListView = new ListView<>(); 
        historyListView.setPrefWidth(450);
        historyListView.setPrefHeight(400); 
        historyListView.setStyle("-fx-background-color: #5872C9; -fx-border-color: #FFFFFF; -fx-border-width: 3;"); 
        historyListView.setCellFactory(listView -> new HistoryCardCell());

        VBox emptyHistoryView = createEmptyHistoryView();
        historyListView.setPlaceholder(emptyHistoryView);

        historyContainer.getChildren().add(historyListView);
        centerPanel.getChildren().add(historyContainer); 
        return centerPanel;
    }

    private VBox createEmptyHistoryView() {
        VBox emptyView = new VBox();
        emptyView.setAlignment(Pos.CENTER);
        emptyView.setSpacing(15);
        emptyView.setStyle("-fx-background-color: transparent;");
        
        Label icon = new Label("🎯");
        icon.setStyle("-fx-font-size: 48px;");
        
        Label title = new Label("No game history");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
        
        Label subtitle = new Label("Play your first game to see your history here!");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #E8E8E8;");
        
        emptyView.getChildren().addAll(icon, title, subtitle);
        return emptyView;
    }
     
    private void loadHistories() {
        if (currentUser != null && currentUser.getHistory() != null) {
            historyListView.getItems().clear();
            historyListView.getItems().addAll(currentUser.getHistory());
        }
    }

    private HBox createBottomPanel() { 
        HBox bottomPanel = new HBox(); 
        bottomPanel.setAlignment(Pos.CENTER); 
        bottomPanel.setPadding(new Insets(0, 0, 15, 0)); 

        Button backButton = new Button("Return"); 
        backButton.setPrefWidth(150); 
        UtilsMenuView.styleButton(backButton, "black", "#333333", "white", "5px 0 5px 0");
        backButton.setOnAction(e -> {
            controller.navigateToGameMenu();
        }); 

        bottomPanel.getChildren().add(backButton); 
        return bottomPanel; 
    } 
     
    private class HistoryCardCell extends ListCell<History> { 
        
	    @Override 
        protected void updateItem(History history, boolean empty) { 
            super.updateItem(history, empty); 
           
            if (empty || history == null) { 
                setGraphic(null); 
		        setStyle("-fx-background-color: transparent;");
            } else { 
                VBox card = createHistoryCard(history); 
                setGraphic(card); 
            	setStyle("-fx-background-color: transparent;");
	        }       
        } 
         
        private VBox createHistoryCard(History history) { 
            
            VBox card = new VBox(); 
            card.setPadding(new Insets(15)); 
            card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 2;"); 
            
            // Determinar si el jugador principal ganó 
            boolean playerWon = history.getWinner().equals(currentUser.getUsername()); 
            String backgroundColor = playerWon ? "#e3f2fd" : "#ffebee";
            String borderColor = playerWon ? "#43fa57" : "#f44336"; 
            
            card.setStyle(card.getStyle() + String.format(" -fx-background-color: %s; -fx-border-color: %s;", backgroundColor, borderColor)); 
            
            HBox playersSection = new HBox(); 
            playersSection.setAlignment(Pos.CENTER); 
            playersSection.setSpacing(20); 
            
            VBox opponentsBox = new VBox(5); 
            opponentsBox.setAlignment(Pos.CENTER_RIGHT);              

            for (User player : history.getPlayers()) { 
                if (!player.getUsername().equals(currentUser.getUsername())) { 
                    Label opponentLabel = new Label("👤 " + player.getUsername()); 
                    
                    opponentLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: black;");        
                    opponentsBox.getChildren().add(opponentLabel); 
                } 
            } 
            
            Label vsLabel = new Label("VS"); 
            vsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #666666;"); 
            
            VBox playerBox = new VBox(5); 
            playerBox.setAlignment(Pos.CENTER_LEFT); 
            
            Label currentPlayerLabel = new Label("👤 " + currentUser.getUsername());         
            currentPlayerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: black;");
                
            playerBox.getChildren().add(currentPlayerLabel);            
            playersSection.getChildren().addAll(playerBox, vsLabel, opponentsBox); 
            
            Label resultLabel = new Label(playerWon ? "¡VICTORY!" : "DEFEAT"); 
            resultLabel.setStyle(String.format("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: %s;",  
                                playerWon ? "#4caf50" : "#f44336")); 
            resultLabel.setAlignment(Pos.CENTER); 
            
            Label timeLabel = new Label("⏱ Time: " + history.getTimePlayed()); 
            timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;"); 
            timeLabel.setAlignment(Pos.CENTER); 
            
            card.getChildren().addAll(playersSection, resultLabel, timeLabel); 
            card.setAlignment(Pos.CENTER); 
            
            return card; 
        }
    } 

    public Scene getScene() { 
        return scene; 
    }
}
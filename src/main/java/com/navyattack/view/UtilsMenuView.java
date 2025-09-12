package com.navyattack.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UtilsMenuView {

	// Método para aplicar estilo a los botones
    	public static void styleButton(Button button) {
        	button.setStyle(
            		"-fx-background-color: black;" +
            		"-fx-text-fill: white;" +
            		"-fx-font-size: 12px;" +
            		"-fx-font-weight: bold;" +
            		"-fx-background-radius: 5;"
        	);
        	button.setMaxWidth(Double.MAX_VALUE);
        
        	button.setOnMouseEntered(e -> 
            		button.setStyle(
                		"-fx-background-color: #333333;" + 
                		"-fx-text-fill: white;" +
                		"-fx-font-size: 12px;" +
                		"-fx-font-weight: bold;" +
                		"-fx-background-radius: 5;"
            		)
        	);
        
        	button.setOnMouseExited(e -> 
            		button.setStyle(
                		"-fx-background-color: black;" +
                		"-fx-text-fill: white;" +
                		"-fx-font-size: 12px;" +
                		"-fx-font-weight: bold;" +
               		 	"-fx-background-radius: 5;"
            		)
        	);
    	}

	public static void styleText(Text text) {
		text.setStyle(
    			"-fx-fill: #0066cc;" +
    			"-fx-font-size: 12px;" +
    			"-fx-font-weight: bold;" +
    			"-fx-underline: true;"
		);
		text.setOnMouseEntered(e -> 
    			text.setStyle(
        			"-fx-fill: #004499;" +
        			"-fx-font-size: 12px;" +
        			"-fx-font-weight: bold;" +
        			"-fx-underline: true;" +
        			"-fx-cursor: hand;"
    			)
		);
		text.setOnMouseExited(e -> 
    			text.setStyle(
        			"-fx-fill: #0066cc;" +
        			"-fx-font-size: 12px;" +
        			"-fx-font-weight: bold;" +
        			"-fx-underline: true;"
    			)
		);
	}

	// Método para mostrar mensajes al usuario
    	public static void showMessage(String message, String type, Text messageLabel) {
        	messageLabel.setText(message);
        
        	if (type.equals("success")) {
            		messageLabel.setStyle("-fx-fill: green;");
        	} else {
            		messageLabel.setStyle("-fx-fill: red;");
        	}
    	}
}
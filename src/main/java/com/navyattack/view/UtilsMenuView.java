package com.navyattack.view;

import javafx.scene.Cursor;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class UtilsMenuView {

    public static void styleButton(Button button, String color, String colorHover, String colorText, String padding) {
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: " + colorText + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
 	        "-fx-padding: " + padding + ";" 
        );
        
        button.setOnMouseEntered(e -> {
                button.setStyle(
                    "-fx-background-color: " + colorHover + ";" +
                    "-fx-text-fill: " + colorText + ";" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 5;" +
		            "-fx-padding: " + padding + ";"
                );
                button.setCursor(Cursor.HAND);
            }
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: " + colorText + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: " + padding + ";"
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

    public static void showMessage(String message, String type, Text messageLabel) {
        messageLabel.setText(message);
        
        if (type.equals("success")) {
            messageLabel.setStyle("-fx-fill: green;");
        } else {
            messageLabel.setStyle("-fx-fill: red;");
        }
    }

    public static ImageView createImage(String path) {
        ImageView iconImage = new ImageView();
        try {
            Image image = new Image(path); 
            iconImage.setImage(image);
            iconImage.setFitWidth(40); 
            iconImage.setFitHeight(40); 
            iconImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + e.getMessage());
        }
        return iconImage;
    }
}